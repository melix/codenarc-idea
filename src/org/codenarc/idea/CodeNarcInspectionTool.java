/*
 *
 *
 *   Copyright 2011 Cédric Champeau
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  /
 * /
 */

package org.codenarc.idea;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.configurationStore.XmlSerializer;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.ParameterizedCachedValue;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.xmlb.XmlSerializationException;
import org.codenarc.idea.ui.Helpers;
import org.codenarc.rule.AbstractRule;
import org.codenarc.rule.Violation;
import org.codenarc.source.SourceCode;
import org.codenarc.source.SourceString;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Base class for CodeNarc violation rules, which will get proxied in order to work with the IntelliJ IDEA inspection
 * plugin mechanism.
 */
public abstract class CodeNarcInspectionTool extends LocalInspectionTool {
    private static final String GROUP_DISPLAY_NAME = "CodeNarc";
    private static final String RULESET_INTRO = " - Ruleset ";
    private static final Key<CachedValue<SourceString>> SOURCE_AS_STRING_CACHE_KEY = Key.create("CODENARC_SOURCE_AS_STRING");
    private static final Key<CachedValue<Boolean>> HAS_SYNTAX_ERRORS_CACHE_KEY = Key.create("CODENARC_HAS_SYNTAX_ERRORS");
    private static final Key<ParameterizedCachedValue<ProblemDescriptor[], AbstractRule>> VIOLATIONS_CACHE_KEY = Key.create("CODENARC_VIOLATIONS");
    private static final AbstractRule UNLOADED_RULE = new AbstractRule() {
        public List<Violation> applyTo(final SourceCode sourceCode) {
            return Collections.emptyList();
        }

        public int getPriority() {
            return 0;
        }

        @Override
        public void setPriority(int priority) {
        }

        public String getName() {
            return "Rule could not be loaded";
        }

        @Override
        public void setName(String name) {
        }

        @Override
        public int getCompilerPhase() {
            return 0;
        }

        @Override
        public void applyTo(SourceCode sourceCode, List<Violation> violations) {
        }
    };

    private static final AbstractRule UNSUPPORTED_RULE = new AbstractRule() {
        public List<Violation> applyTo(final SourceCode sourceCode) {
            return Collections.emptyList();
        }

        public int getPriority() {
            return 0;
        }

        @Override
        public void setPriority(int priority) {
        }

        public String getName() {
            return "Extended rule is not supported";
        }

        @Override
        public void setName(String name) {
        }

        @Override
        public int getCompilerPhase() {
            return 0;
        }

        @Override
        public void applyTo(SourceCode sourceCode, List<Violation> violations) {
        }
    };

    private ResourceBundle bundle;

    private String shortName;
    private String displayName;
    private String description;

    @SuppressWarnings({"WeakerAccess"})
    protected AbstractRule rule;

    public AbstractRule getRule() {
        return rule;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getDoNotApplyToFilesMatching() { return rule.getDoNotApplyToFilesMatching(); }

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

    public CodeNarcInspectionTool() {
        initRule();
    }

    private String getRuleDescriptionOrDefaultMessage(AbstractRule rule) {
        String resourceKey = rule.getName() + ".description.html";
        return getResourceBundleString(resourceKey, "No description provided for rule named [" + rule.getName() + "]");
    }

    private String getResourceBundleString(String resourceKey, String defaultString) {
        String string;
        try {
            string = bundle.getString(resourceKey);
        } catch (MissingResourceException e) {
            string = defaultString;
        }
        return string;
    }

    @SuppressWarnings("WeakerAccess")
    protected abstract String getRuleClass();

    public abstract String getRuleset();

    @Override
    public JPanel createOptionsPanel() {
        return Helpers.createOptionsPanel(this);
    }

    @Override
    public void writeSettings(@NotNull Element node) {
        XmlSerializer.serializeObjectInto(this.rule, node, getSerializationFilter());
    }

    @Override
    public void readSettings(@NotNull Element node) {
        try {
            if (node.getChild("option") != null) {
                XmlSerializer.deserializeInto(node, this.rule);
            }
        }
        catch (XmlSerializationException e) {
            throw new InvalidDataException(e);
        }
    }

    private void initRule() {
        try {
            rule = getRuleInstance();
            if (rule.getCompilerPhase() > 3) {
                defineUnsupportedRule();
                return;
            }
            String ruleName = rule.getName();
            shortName = ruleName != null ? ruleName : rule.getClass().getSimpleName();
            displayName = ruleName != null ? Helpers.camelCaseToSentence(ruleName) : rule.getClass().getSimpleName();
            description = getRuleDescriptionOrDefaultMessage(rule);
        } catch (Throwable e) {
            defineErrorRule(e);
        }
    }

    private AbstractRule getRuleInstance() throws Throwable {
        bundle = ResourceBundle.getBundle(CodeNarcComponent.BASE_MESSAGES_BUNDLE);
        Class<?> clazz = Helpers.getRuleClassInstance(getRuleClass());
        return (AbstractRule) clazz.newInstance();
    }

    private void defineErrorRule(Throwable e) {
        rule = UNLOADED_RULE;
        shortName = "RuleCannotBeLoaded_"+getRuleClass().replaceAll("[^a-zA-Z0-9]","_");
        displayName = "Not loaded: " + getRuleClass();
        StringWriter wrt = new StringWriter();
        e.printStackTrace(new PrintWriter(wrt));
        description = "Unable to load rule : "+wrt.toString();
    }

    private void defineUnsupportedRule() {
        rule = UNSUPPORTED_RULE;
        shortName = "UnsupportedRule_" + getRuleClass().replaceAll("[^a-zA-Z0-9]","_");
        displayName = "Unsupported: " + getRuleClass();
        description = "Extended rules need to be run in compiler phase 4, which does not happen while editing";
    }

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return GROUP_DISPLAY_NAME + RULESET_INTRO + getRuleset();
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public String getStaticDescription() {
        return description;
    }

    @Override
    public ProblemDescriptor[] checkFile(@NotNull final PsiFile file, @NotNull final InspectionManager manager, final boolean isOnTheFly) {
        if (file.getFileType().getName().equalsIgnoreCase("groovy")) {
            final CachedValuesManager cachedValuesManager = CachedValuesManager.getManager(manager.getProject());
            CachedValue<Boolean> hasErrorsCachedValue = file.getUserData(HAS_SYNTAX_ERRORS_CACHE_KEY);
            if (hasErrorsCachedValue == null) {
                hasErrorsCachedValue = cachedValuesManager.createCachedValue(() -> {
                    PsiErrorElement errorElement = PsiTreeUtil.findChildOfType(file, PsiErrorElement.class);
                    return CachedValueProvider.Result.create(errorElement != null, file);
                }, false);
            }

            if (!hasErrorsCachedValue.getValue()) { // avoid inspection if any syntax error is found
                ParameterizedCachedValue<ProblemDescriptor[], AbstractRule> cachedViolations = file.getUserData(VIOLATIONS_CACHE_KEY);
                if (cachedViolations == null) {
                    cachedViolations = cachedValuesManager.createParameterizedCachedValue(rule -> {
                        final List<ProblemDescriptor> descriptors = new LinkedList<>();
                        CachedValue<SourceString> sourceStringCachedValue = file.getUserData(SOURCE_AS_STRING_CACHE_KEY);
                        if (sourceStringCachedValue == null) {
                            sourceStringCachedValue = cachedValuesManager.createCachedValue(() -> {
                                if (file.getText() == null || "".equals(file.getText())) return null;
                                return CachedValueProvider.Result.create(new SourceString(file.getText()), file);
                            }, false);
                            file.putUserData(SOURCE_AS_STRING_CACHE_KEY, sourceStringCachedValue);
                        }
                        final SourceCode code = sourceStringCachedValue.getValue();
                        if (code != null) {
                            try {
                                final List<Violation> list = rule.applyTo(code);
                                if (list != null) {
                                    final FileDocumentManager documentManager = FileDocumentManager.getInstance();
                                    final VirtualFile virtualFile = file.getVirtualFile();
                                    if (virtualFile != null) {
                                        final Document document = documentManager.getDocument(virtualFile);
                                        for (final Violation violation : list) {
                                            Integer lineNumber = violation.getLineNumber();
                                             // workaround for some rules which do not set the line number correctly
                                            if (lineNumber==null || lineNumber < 1) {
                                                lineNumber = 1;
                                            }
                                            final int startOffset = document.getLineStartOffset(lineNumber - 1) < 0 ? 0 : document.getLineStartOffset(lineNumber - 1);
                                            final String message = violation.getMessage();
                                            final String violatedLine = document.getText(new TextRange(startOffset, document.getLineEndOffset(lineNumber)));
                                            final String sourceLine = violation.getSourceLine();
                                            int violationPosition = violatedLine.indexOf(sourceLine);

                                            final LocalQuickFix [] localQuickFixes = CodeNarcUiMappings.getQuickFixesFor(violation);
                                            ProblemDescriptor descriptor;

                                            TextRange violatingRange = new TextRange(startOffset + violationPosition, startOffset + violationPosition + sourceLine.length());

                                            final PsiElement violatingElement = PsiUtil.getElementInclusiveRange(file, violatingRange);
                                            if (violatingElement != null && this.isSuppressedFor(violatingElement)) {
                                                continue;
                                            }

                                            descriptor = manager.createProblemDescriptor(
                                                    file,
                                                    violatingRange,
                                                    message == null ? description == null ? rule.getName() : description : message,
                                                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                                    isOnTheFly,
                                                    localQuickFixes);
                                            descriptors.add(descriptor);
                                        }
                                    }
                                    return CachedValueProvider.Result.create(descriptors.toArray(new ProblemDescriptor[0]), file);
                                }
                            } catch (Throwable throwable) {
                                return null;
                            }
                            return null;
                        } else {
                            return null;
                        }
                    }, false);
                }
                return cachedViolations.getValue(rule);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
