package org.codenarc.idea;

import com.intellij.codeInspection.InjectionAwareSuppressQuickFix;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.InspectionSuppressor;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.configurationStore.XmlSerializer;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.diagnostic.ControlFlowException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.ParameterizedCachedValue;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.ThreeState;
import com.intellij.util.xmlb.XmlSerializationException;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import org.codenarc.idea.disablerules.DisabledRulesService;
import org.codenarc.idea.ui.Helpers;
import org.codenarc.rule.AbstractRule;
import org.codenarc.rule.Violation;
import org.codenarc.source.SourceCode;
import org.codenarc.source.SourceString;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import javax.swing.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Base class for CodeNarc violation rules, which will get proxied in order to work with the IntelliJ IDEA inspection
 * plugin mechanism.
 */
public abstract class CodeNarcInspectionTool<R extends AbstractRule> extends LocalInspectionTool {

    public static final String BASE_MESSAGES_BUNDLE = "codenarc-base-messages";
    public static final String GROUP_DISPLAY_NAME = "CodeNarc";

    protected static final String SPECIFICATION_CLASSES = "*Spec,*Specification";
    protected static final String GRADLE_FILES = "*.gradle";

    private static final Logger LOG = Logger.getInstance(CodeNarcInspectionTool.class);
    private static final Key<CachedValue<SourceString>> SOURCE_AS_STRING_CACHE_KEY =
        Key.create("CODENARC_SOURCE_AS_STRING");
    private static final Key<CachedValue<Boolean>> HAS_SYNTAX_ERRORS_CACHE_KEY =
        Key.create("CODENARC_HAS_SYNTAX_ERRORS");
    private static final Key<ParameterizedCachedValue<ProblemDescriptor[], AbstractRule>> VIOLATIONS_CACHE_KEY =
        Key.create("CODENARC_VIOLATIONS");
    private final R rule;
    private final ResourceBundle bundle = ResourceBundle.getBundle(BASE_MESSAGES_BUNDLE);
    private final String description;

    protected CodeNarcInspectionTool(R rule) {
        this.rule = rule;
        this.description = getRuleDescriptionOrDefaultMessage(rule);
    }

    public static String getShortName(AbstractRule rule) {
        String ruleName = rule.getName();
        return GROUP_DISPLAY_NAME + "." + (ruleName != null ? ruleName : rule.getClass().getSimpleName());
    }

    public static String getDisplayName(AbstractRule rule) {
        String ruleName = rule.getName();
        return ruleName != null ? Helpers.camelCaseToSentence(ruleName) : rule.getClass().getSimpleName();
    }

    public R getRule() {
        return rule;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getDoNotApplyToFilesMatching() {
        return rule.getDoNotApplyToFilesMatching();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setDoNotApplyToFilesMatching(String value) {
        rule.setDoNotApplyToFilesMatching(value);
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getApplyToFilesMatching() {
        return rule.getApplyToFilesMatching();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setApplyToFileMatching(String value) {
        rule.setApplyToFilesMatching(value);
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getDoNotApplyToFileNames() {
        return rule.getDoNotApplyToFileNames();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setDoNotApplyToFileNames(String value) {
        rule.setDoNotApplyToFileNames(value);
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getApplyToFileNames() {
        return rule.getApplyToFileNames();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setApplyToFileNames(String value) {
        rule.setApplyToFileNames(value);
    }

    private String getRuleDescriptionOrDefaultMessage(final AbstractRule rule) {
        String resourceKey = rule.getName() + ".description.html";
        return "[" + rule.getName() + "] " + getResourceBundleString(resourceKey, "No description provided");
    }

    private String getResourceBundleString(
        @PropertyKey(resourceBundle = BASE_MESSAGES_BUNDLE) String resourceKey,
        String defaultString
    ) {
        String string;
        try {
            string = bundle.getString(resourceKey);
        } catch (MissingResourceException ignored) {
            string = defaultString;
        }

        return string;
    }

    public abstract String getRuleset();

    @Override
    public @NonNls @Nullable String getAlternativeID() {
        return (rule.getName() != null ? rule.getName() : rule.getClass().getSimpleName());
    }

    @Override
    public JPanel createOptionsPanel() {
        return Helpers.createOptionsPanel(this);
    }

    @Override
    public void writeSettings(@NotNull Element node) {
        XmlSerializer.serializeObjectInto(this.rule, node);
    }

    @Override
    public void readSettings(@NotNull Element node) {
        try {
            if (node.getChild("option") != null) {
                XmlSerializer.deserializeInto(node, this.rule);
            } else {
                applyDefaultConfiguration(this.rule);
            }

        } catch (XmlSerializationException e) {
            throw new InvalidDataException(e);
        }

    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) @NotNull String getGroupDisplayName() {
        return getRuleset();
    }

    @NonNls
    @Override
    public @Nullable String getGroupKey() {
        return getRuleset();
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) String @NotNull [] getGroupPath() {
        return new String[]{GROUP_DISPLAY_NAME, getRuleset()};
    }

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return description;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProblemDescriptor[] checkFile(
        @NotNull final PsiFile file,
        @NotNull final InspectionManager manager,
        final boolean isOnTheFly
    ) {
        if (!file.getFileType().getName().equalsIgnoreCase("groovy")) {
            return null;
        }

        final CachedValuesManager cachedValuesManager = CachedValuesManager.getManager(manager.getProject());

        if (readErrorsCachedValue(file, cachedValuesManager).getValue() == Boolean.TRUE) {
            // avoid inspection if any syntax error is found
            return null;
        }

        ParameterizedCachedValue<ProblemDescriptor[], AbstractRule> cachedViolations = file
            .getUserData(VIOLATIONS_CACHE_KEY);

        if (cachedViolations != null) {
            return null;
        }

        // file.putUserData(VIOLATIONS_CACHE_KEY, cachedViolations);

        cachedViolations = cachedValuesManager.createParameterizedCachedValue(r -> {
            final SourceCode code = computeCachedDataIfAbsent(
                file,
                cachedValuesManager,
                SOURCE_AS_STRING_CACHE_KEY, () -> {
                    if (file.getText() == null || file.getText().isEmpty()) {
                        return null;
                    }
                    VirtualFile virtualFile = file.getVirtualFile();
                    return new SourceString(
                        file.getText(),
                        virtualFile == null ? null : virtualFile.getCanonicalPath(),
                        file.getName()
                    );
                }
            );
            try {
                return CachedValueProvider.Result.create(doCheckFile(file, manager, isOnTheFly, code, (R) r), file);
            } catch (Throwable e) {
                if (!(e instanceof ControlFlowException)) {
                    LOG.error("Exception checking rule " + r, e);
                }

                return null;
            }
        }, false);

        return cachedViolations.getValue(rule);
    }

    // this method is nearly the same as the on in superclass but it uses altarnative ID (without CodeNarc) prefix
    // to be aligned with CodeNarc behaviour
    public SuppressQuickFix @NotNull [] getBatchSuppressActions(@Nullable PsiElement element) {
        if (element == null) {
            return SuppressQuickFix.EMPTY_ARRAY;
        }
        Set<SuppressQuickFix> fixes = new ObjectOpenCustomHashSet<>(new Hash.Strategy<>() {
            @Override
            public int hashCode(@Nullable SuppressQuickFix object) {
                if (object == null) {
                    return 0;
                }
                int result = object instanceof InjectionAwareSuppressQuickFix
                        ? ((InjectionAwareSuppressQuickFix)object).isShouldBeAppliedToInjectionHost().hashCode()
                        : 0;
                return 31 * result + object.getName().hashCode();
            }

            @Override
            public boolean equals(SuppressQuickFix o1, SuppressQuickFix o2) {
                if (o1 == o2) {
                    return true;
                }
                if (o1 == null || o2 == null) {
                    return false;
                }

                if (o1 instanceof InjectionAwareSuppressQuickFix && o2 instanceof InjectionAwareSuppressQuickFix) {
                    if (((InjectionAwareSuppressQuickFix)o1).isShouldBeAppliedToInjectionHost() !=
                            ((InjectionAwareSuppressQuickFix)o2).isShouldBeAppliedToInjectionHost()) {
                        return false;
                    }
                }
                return o1.getName().equals(o2.getName());
            }
        });

        Set<InspectionSuppressor> suppressors = new HashSet<>(getSuppressors(element));
        final PsiLanguageInjectionHost injectionHost = InjectedLanguageManager.getInstance(element.getProject())
            .getInjectionHost(element);
        if (injectionHost != null) {
            Set<InspectionSuppressor> injectionHostSuppressors = new HashSet<>(getSuppressors(injectionHost));
            for (InspectionSuppressor suppressor : injectionHostSuppressors) {
                addAllSuppressActions(fixes, injectionHost, suppressor, ThreeState.YES, getAlternativeID());
            }
        }

        for (InspectionSuppressor suppressor : suppressors) {
            addAllSuppressActions(
                fixes,
                element,
                suppressor,
                injectionHost != null ? ThreeState.NO : ThreeState.UNSURE,
                getAlternativeID()
            );
        }
        return fixes.toArray(SuppressQuickFix.EMPTY_ARRAY);
    }

    protected abstract @NotNull Collection<LocalQuickFix> getQuickFixesFor(
        Violation violation,
        PsiElement violatingElement
    );

    protected void applyDefaultConfiguration(R rule) {
        // allows override the defaults in the subclasses
    }

    @NotNull
    protected TextRange convertViolationRangeToRelative(PsiElement violatingElement, TextRange violatingRange) {
        final int relativeRangeStart = violatingElement.getTextRange().getStartOffset() -
            violatingRange.getStartOffset();
        final int relativeRangeEnd = violatingElement.getTextRange().getEndOffset() - violatingRange.getStartOffset();
        return new TextRange(Math.max(0, relativeRangeStart), relativeRangeEnd);
    }

    @Nullable
    protected PsiElement extractViolatingElement(@NotNull PsiFile file, TextRange violatingRange) {
        return PsiUtil.getElementInclusiveRange(file, violatingRange);
    }

    @NotNull
    protected TextRange extractViolatingRange(Document document, Violation violation) {
        final int lineNumber = extractLineNumber(violation);
        final int startOffset = Math.max(document.getLineStartOffset(lineNumber - 1), 0);
        TextRange defaultRange = new TextRange(startOffset, document.getLineEndOffset(lineNumber - 1));
        final String violatedLine = document.getText(defaultRange);
        final String sourceLine = violation.getSourceLine();

        if (StringUtil.isNotEmpty(sourceLine)) {
            int violationPosition = violatedLine.indexOf(sourceLine);
            int violationStart = Math.max(startOffset + violationPosition, 0);
            int violationEnd = Math.min(
                startOffset + violationPosition + sourceLine.length(),
                document.getTextLength() - 1
            );
            return new TextRange(violationStart, violationEnd);
        }

        LOG.warn("Missing source line for violation " + violation + " in the document " + document);

        return defaultRange;
    }

    protected String extractMessage(Violation violation, R r) {
        final String message = violation.getMessage();
        String currentDescription = description == null ? r.getName() : description;
        return message == null ? currentDescription : message;
    }

    protected int extractLineNumber(Violation violation) {
        Integer lineNumber = violation.getLineNumber();

        // workaround for some rules which do not set the line number correctly
        // these should roverride this method
        if (lineNumber == null || lineNumber < 1) {
            return 1;
        }

        return lineNumber;
    }


    private static void addAllSuppressActions(@NotNull Collection<? super SuppressQuickFix> fixes,
                                              @NotNull PsiElement element,
                                              @NotNull InspectionSuppressor suppressor,
                                              @NotNull ThreeState appliedToInjectionHost,
                                              @NotNull String toolId) {
        final SuppressQuickFix[] actions = suppressor.getSuppressActions(element, toolId);
        for (SuppressQuickFix action : actions) {
            if (action instanceof InjectionAwareSuppressQuickFix) {
                ((InjectionAwareSuppressQuickFix)action).setShouldBeAppliedToInjectionHost(appliedToInjectionHost);
            }
            fixes.add(action);
        }
    }

    @Nullable
    private ProblemDescriptor[] doCheckFile(
        @NotNull PsiFile file,
        @NotNull InspectionManager manager,
        boolean isOnTheFly,
        SourceCode code,
        R r
    ) throws Throwable {
        if (code == null) {
            return null;
        }

        final List<Violation> list = r.applyTo(code);

        if (list == null || list.isEmpty()) {
            return null;
        }

        final VirtualFile virtualFile = file.getVirtualFile();

        if (virtualFile == null) {
            return null;
        }

        final FileDocumentManager documentManager = FileDocumentManager.getInstance();
        final Document document = documentManager.getDocument(virtualFile);

        if (document == null) {
            return null;
        }

        try {
            return list
                    .stream()
                    .map(violation ->
                        convertViolationToProblemDescriptor(file, manager, isOnTheFly, r, document, violation)
                    )
                    .filter(Objects::nonNull)
                    .toArray(ProblemDescriptor[]::new);
        } catch (Throwable error) {
            if (error instanceof ProcessCanceledException) {
                LOG.info("Process cancelled!", error);
                return null;
            }
            throw new IllegalStateException(
                "Exception validating file " + file + " by rule " + r + " with code\n" + code,
                error
            );
        }


    }

    @Nullable
    private ProblemDescriptor convertViolationToProblemDescriptor(
        @NotNull PsiFile file,
        @NotNull InspectionManager manager,
        boolean isOnTheFly,
        R r,
        Document document,
        Violation violation
    ) {
        final TextRange violatingRange = extractViolatingRange(document, violation);
        final PsiElement violatingElement = extractViolatingElement(file, violatingRange);

        if (
            violatingElement == null || isSuppressedFor(violatingElement) || (
                DisabledRulesService.getInstance().isRuleDisabled(violation.getRule(), file, violation.getLineNumber())
            )
        ) {
            return null;
        }

        return manager.createProblemDescriptor(
                violatingElement,
                convertViolationRangeToRelative(violatingElement, violatingRange),
                extractMessage(violation, r),
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                isOnTheFly,
                getQuickFixesFor(violation, violatingElement).toArray(new LocalQuickFix[0])
        );
    }

    @NotNull
    private CachedValue<Boolean> readErrorsCachedValue(@NotNull PsiFile file, CachedValuesManager cachedValuesManager) {
        CachedValue<Boolean> hasErrorsCachedValue = file.getUserData(HAS_SYNTAX_ERRORS_CACHE_KEY);
        if (hasErrorsCachedValue == null) {
            hasErrorsCachedValue = cachedValuesManager.createCachedValue(() -> {
                PsiErrorElement errorElement = PsiTreeUtil.findChildOfType(file, PsiErrorElement.class);
                return CachedValueProvider.Result.create(errorElement != null, file);
            }, false);
        }
        return hasErrorsCachedValue;
    }

    private <T> T computeCachedDataIfAbsent(
        @NotNull PsiFile file,
        CachedValuesManager cachedValuesManager,
        Key<CachedValue<T>> key,
        Supplier<T> supplier
    ) {
        CachedValue<T> cachedValue = file.getUserData(key);
        if (cachedValue == null) {
            cachedValue = cachedValuesManager.createCachedValue(
                () -> CachedValueProvider.Result.create(supplier.get(), file),
                false
            );
            file.putUserData(key, cachedValue);
        }
        return cachedValue.getValue();
    }
}
