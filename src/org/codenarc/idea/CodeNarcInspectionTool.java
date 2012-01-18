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
/**
 * Created by IntelliJ IDEA.
 * User: cedric
 * Date: 20/01/11
 * Time: 23:09
 */

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.*;
import org.codenarc.rule.AbstractRule;
import org.codenarc.rule.Rule;
import org.codenarc.rule.Violation;
import org.codenarc.source.SourceCode;
import org.codenarc.source.SourceString;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Base class for CodeNarc violation rules, which will get proxied in order to work with the IntelliJ IDEA inspection
 * plugin mechanism.
 */
public abstract class CodeNarcInspectionTool extends LocalInspectionTool {
    private static final String GROUP_DISPLAY_NAME = "CodeNarc";
    private static final String RULESET_INTRO = " - Ruleset ";
    private static final Key<CachedValue<SourceString>> SOURCE_AS_STRING_CACHE_KEY = Key.<CachedValue<SourceString>>create("CODENARC_SOURCE_AS_STRING");
    private static final Key<CachedValue<Boolean>> HAS_SYNTAX_ERRORS_CACHE_KEY = Key.<CachedValue<Boolean>>create("CODENARC_HAS_SYNTAX_ERRORS");
    private static final Key<ParameterizedCachedValue<ProblemDescriptor[], Rule>> VIOLATIONS_CACHE_KEY = Key.<ParameterizedCachedValue<ProblemDescriptor[], Rule>>create("CODENARC_VIOLATIONS");
    private static final Rule UNLOADED_RULE = new Rule() {
        public List<Violation> applyTo(final SourceCode sourceCode) throws Throwable {
            return Collections.emptyList();
        }

        public int getPriority() {
            return 0;
        }

        public String getName() {
            return "Rule could not be loaded";
        }
    };

    private ResourceBundle bundle;

    private String shortName;
    private String displayName;
    private String description;
    protected Rule rule;

    public CodeNarcInspectionTool() {
        initRule();
    }

    private String getRuleDescriptionOrDefaultMessage(Rule rule) {
        String resourceKey = rule.getName() + ".description";
        return getResourceBundleString(resourceKey, "No description provided for rule named [" + rule.getName() + "]");
    }

    protected String getResourceBundleString(String resourceKey, String defaultString) {
        String string;
        try {
            string = bundle.getString(resourceKey);
        } catch (MissingResourceException e) {
            string = defaultString;
        }
        return string;
    }

    protected abstract String getRuleClass();

    protected abstract String getRuleset();

    private void initRule() {
        try {
            bundle = ResourceBundle.getBundle(CodeNarcComponent.BASE_MESSAGES_BUNDLE);
            ClassLoader classLoader = CodeNarcInspectionTool.class.getClassLoader();
            Class<?> clazz = Class.forName(getRuleClass(), true, classLoader);
            rule = (Rule) clazz.newInstance();
            String ruleName = rule.getName();
            shortName = ruleName != null ? ruleName : rule.getClass().getSimpleName();
            displayName = ruleName != null ? ruleName : rule.getClass().getSimpleName();
            description = getRuleDescriptionOrDefaultMessage(rule);
        } catch (InstantiationException e) {
            defineErrorRule(e);
        } catch (IllegalAccessException e) {
            defineErrorRule(e);
        } catch (ClassNotFoundException e) {
            defineErrorRule(e);
        } catch (Throwable e) {
            defineErrorRule(e);
        }
    }

    private void defineErrorRule(Throwable e) {
        rule = UNLOADED_RULE;
        shortName = "RuleCannotBeLoaded_"+getRuleClass().replaceAll("[^a-zA-Z0-9]","_");
        displayName = "Not loaded: "+getRuleClass();
        StringWriter wrt = new StringWriter();
        e.printStackTrace(new PrintWriter(wrt));
        description = "Unable to load rule : "+wrt.toString();
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
                hasErrorsCachedValue = cachedValuesManager.createCachedValue(new CachedValueProvider<Boolean>() {
                    public Result<Boolean> compute() {
                        PsiErrorElement errorElement = PsiTreeUtil.findChildOfType(file, PsiErrorElement.class);
                        return Result.create(errorElement != null, file);
                    }
                }, false);
            }

            if (!hasErrorsCachedValue.getValue()) { // avoid inspection if any syntax error is found
                ParameterizedCachedValue<ProblemDescriptor[], Rule> cachedViolations = file.getUserData(VIOLATIONS_CACHE_KEY);
                if (cachedViolations == null) {
                    cachedViolations = cachedValuesManager.createParameterizedCachedValue(new ParameterizedCachedValueProvider<ProblemDescriptor[], Rule>() {
                        public CachedValueProvider.Result<ProblemDescriptor[]> compute(final Rule rule) {
                            final List<ProblemDescriptor> descriptors = new LinkedList<ProblemDescriptor>();
                            CachedValue<SourceString> sourceStringCachedValue = file.getUserData(SOURCE_AS_STRING_CACHE_KEY);
                            if (sourceStringCachedValue == null) {
                                sourceStringCachedValue = cachedValuesManager.createCachedValue(new CachedValueProvider<SourceString>() {
                                    public Result<SourceString> compute() {
                                        if (file.getText() == null || "".equals(file.getText())) return null;
                                        return Result.create(new SourceString(file.getText()), file);
                                    }
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
                                            for (final Violation violation : list) {
                                                Document document = documentManager.getDocument(virtualFile);
                                                Integer lineNumber = violation.getLineNumber();
                                                 // workaround for some rules which do not set the line number correctly
                                                if (lineNumber==null) lineNumber=1;
                                                final int startOffset = document.getLineStartOffset(lineNumber - 1);
                                                final String message = violation.getMessage();
                                                PsiElement element = PsiUtil.getElementAtOffset(file, startOffset);
                                                ProblemDescriptor descriptor = manager.createProblemDescriptor(
                                                        element,
                                                        message == null ? description == null ? rule.getName() : description : message,
                                                        ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                                                        null,
                                                        isOnTheFly);
                                                descriptors.add(descriptor);
                                            }
                                        }
                                        return CachedValueProvider.Result.create(descriptors.toArray(new ProblemDescriptor[descriptors.size()]), file);
                                    }
                                } catch (Throwable throwable) {
                                    return null;
                                }
                                return null;
                            } else {
                                return null;
                            }
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
