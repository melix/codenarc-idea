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
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtil;
import org.codenarc.rule.Rule;
import org.codenarc.rule.Violation;
import org.codenarc.source.SourceString;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

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

    protected abstract Class getRuleClass();

    private void initRule() {
        try {
            bundle = ResourceBundle.getBundle(CodeNarcComponent.BASE_MESSAGES_BUNDLE);
            rule = (Rule) getRuleClass().newInstance();
            String ruleName = rule.getName();
            shortName = ruleName != null ? ruleName : rule.getClass().getSimpleName();
            displayName = ruleName != null ? ruleName : rule.getClass().getSimpleName();
            description = getRuleDescriptionOrDefaultMessage(rule);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return GROUP_DISPLAY_NAME;
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
            final Editor editor = PsiUtil.findEditor(file);
            final List<ProblemDescriptor> descriptors = new LinkedList<ProblemDescriptor>();
            try {
                final List<Violation> list = rule.applyTo(new SourceString(file.getText()));
                for (final Violation violation : list) {
                    final int startOffset = editor.getDocument().getLineStartOffset(violation.getLineNumber() - 1);
                    final String message = violation.getMessage();
                    final PsiElement element = PsiUtil.getElementAtOffset(file, startOffset);
                    ProblemDescriptor descriptor = manager.createProblemDescriptor(
                            element,
                            message == null ? description==null?rule.getName():description : message,
                            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                            null,
                            isOnTheFly);
                    descriptors.add(descriptor);
                }
            } catch (Throwable e) {
                /* Probably a syntax error */
            }
            return descriptors.toArray(new ProblemDescriptor[descriptors.size()]);
        } else {
            return null;
        }
    }

    private ProblemHighlightType type() {
        switch (rule.getPriority()) {
            case 1:
                return ProblemHighlightType.ERROR;
            default:
                return ProblemHighlightType.ERROR;
        }
    }
}
