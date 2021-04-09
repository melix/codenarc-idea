package org.codenarc.idea;

import com.intellij.codeInsight.daemon.impl.quickfix.AddDefaultConstructorFix;
import com.intellij.codeInsight.daemon.impl.quickfix.DeleteElementFix;
import com.intellij.codeInsight.intention.AddAnnotationPsiFix;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.util.containers.JBIterable;
import com.siyeh.ig.fixes.IntroduceConstantFix;
import groovy.transform.CompileDynamic;
import groovy.transform.CompileStatic;
import org.codenarc.idea.quickfix.ConvertGStringToStringReusableIntention;
import org.codenarc.idea.quickfix.IntentionQuickFix;
import org.codenarc.idea.quickfix.RemoveRedundantClassPropertyReusableIntention;
import org.codenarc.idea.quickfix.RemoveUnnecessaryReturnReusableIntention;
import org.codenarc.idea.quickfix.ReplaceStatementFix;
import org.codenarc.idea.quickfix.ReusableIntention;
import org.codenarc.rule.AbstractRule;
import org.codenarc.rule.Violation;
import org.codenarc.rule.basic.AssignmentInConditionalRule;
import org.codenarc.rule.basic.DeadCodeRule;
import org.codenarc.rule.convention.CompileStaticRule;
import org.codenarc.rule.design.AbstractClassWithPublicConstructorRule;
import org.codenarc.rule.design.AbstractClassWithoutAbstractMethodRule;
import org.codenarc.rule.dry.DuplicateStringLiteralRule;
import org.codenarc.rule.exceptions.ConfusingClassNamedExceptionRule;
import org.codenarc.rule.formatting.BlankLineBeforePackageRule;
import org.codenarc.rule.formatting.BlockEndsWithBlankLineRule;
import org.codenarc.rule.formatting.BlockStartsWithBlankLineRule;
import org.codenarc.rule.formatting.ConsecutiveBlankLinesRule;
import org.codenarc.rule.groovyism.AssignCollectionSortRule;
import org.codenarc.rule.groovyism.AssignCollectionUniqueRule;
import org.codenarc.rule.naming.AbstractClassNameRule;
import org.codenarc.rule.naming.ClassNameRule;
import org.codenarc.rule.naming.ClassNameSameAsFilenameRule;
import org.codenarc.rule.naming.ClassNameSameAsSuperclassRule;
import org.codenarc.rule.naming.ConfusingMethodNameRule;
import org.codenarc.rule.unnecessary.UnnecessaryDotClassRule;
import org.codenarc.rule.unnecessary.UnnecessaryGStringRule;
import org.codenarc.rule.unnecessary.UnnecessaryGetterRule;
import org.codenarc.rule.unnecessary.UnnecessaryReturnKeywordRule;
import org.codenarc.rule.unnecessary.UnnecessarySetterRule;
import org.codenarc.rule.unused.UnusedVariableRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.codeInspection.bugs.GrModifierFix;
import org.jetbrains.plugins.groovy.codeInspection.bugs.GrRemoveModifierFix;
import org.jetbrains.plugins.groovy.codeInspection.naming.RenameFix;
import org.jetbrains.plugins.groovy.codeInspection.style.JavaStylePropertiesInvocationFixer;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrAssignmentExpression;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class CodeNarcUiMappings {

    private static final Logger LOG = Logger.getInstance(CodeNarcUiMappings.class);
    private static final Map<Class<? extends AbstractRule>, Function<PsiElement, List<LocalQuickFix>>> MAPPINGS = new HashMap<>();

    static {
        registerQuickFixes(AbstractClassNameRule.class,
                new GrModifierFix(CodeNarcBundle.message("add.modifier", "abstract"), PsiModifier.ABSTRACT, true, GrModifierFix.MODIFIER_LIST_CHILD),
                new RenameFix()
        );

        registerQuickFixes(AbstractClassWithPublicConstructorRule.class, new GrRemoveModifierFix(PsiModifier.PUBLIC));

        registerQuickFixes(AbstractClassWithoutAbstractMethodRule.class, element -> {
            PsiClass theClass = JBIterable.generate(element, PsiElement::getParent).takeWhile(e -> !(e instanceof PsiFile)).filter(PsiClass.class).first();
            if (theClass == null) {
                return Collections.singletonList(new GrRemoveModifierFix(PsiModifier.ABSTRACT));
            }
            return Arrays.asList(
                    new GrModifierFix(theClass, PsiModifier.ABSTRACT, true, false, GrModifierFix.MODIFIER_LIST_CHILD),
                    new AddDefaultConstructorFix(theClass, PsiModifier.PROTECTED)
            );
        });

        registerQuickFixes(AssignCollectionSortRule.class, new ReplaceStatementFix(GrMethodCall.class, "sort()", "sort(false)"));

        registerQuickFixes(AssignCollectionUniqueRule.class, new ReplaceStatementFix(GrMethodCall.class, "unique()", "unique(false)"));

        registerQuickFixes(AssignmentInConditionalRule.class, new ReplaceStatementFix(GrAssignmentExpression.class, "=", "=="));

        registerQuickFix(BlankLineBeforePackageRule.class, DeleteElementFix::new);

        registerQuickFix(BlockEndsWithBlankLineRule.class, DeleteElementFix::new);

        registerQuickFix(BlockStartsWithBlankLineRule.class, DeleteElementFix::new);

        registerQuickFixes(ClassNameRule.class, new RenameFix());

        registerQuickFixes(ClassNameSameAsFilenameRule.class, new RenameFix());

        registerQuickFixes(ClassNameSameAsSuperclassRule.class, new RenameFix());

        registerQuickFixes(CompileStaticRule.class, element -> {
            if (element instanceof PsiModifierListOwner) {
                return Arrays.asList(
                        new AddAnnotationPsiFix(CompileStatic.class.getName(), (PsiModifierListOwner) element),
                        new AddAnnotationPsiFix(CompileDynamic.class.getName(), (PsiModifierListOwner) element),
                        new AddAnnotationPsiFix("grails.compiler.GrailsCompileStatic", (PsiModifierListOwner) element)
                );
            }
            return Collections.emptyList();
        });

        registerQuickFixes(ConfusingClassNamedExceptionRule.class, new RenameFix());

        registerQuickFixes(ConfusingMethodNameRule.class, new RenameFix());

        registerQuickFix(ConsecutiveBlankLinesRule.class, DeleteElementFix::new);

        registerQuickFix(DeadCodeRule.class, DeleteElementFix::new);

        registerQuickFixes(DuplicateStringLiteralRule.class, new IntroduceConstantFix() {
            @Override
            public PsiElement getElementToRefactor(PsiElement element) {
                for (PsiElement child : element.getChildren()) {
//                    if (child instanceof GrLiteral) {
//                        return child;
//                    }
                    System.out.println(element.getClass().getName() + " expression: " + (element instanceof PsiExpression) + ", variable: " + (element instanceof PsiLocalVariable));
                    PsiElement recursive = getElementToRefactor(child);
                    if (recursive != child) {
                        return recursive;
                    }
                }
                return element;
            }
        });

        registerQuickFix(UnnecessaryDotClassRule.class, new RemoveRedundantClassPropertyReusableIntention());

        registerQuickFix(UnnecessaryGStringRule.class, new ConvertGStringToStringReusableIntention());

        registerQuickFixes(UnnecessaryGetterRule.class, new JavaStylePropertiesInvocationFixer());


        registerQuickFix(UnnecessaryReturnKeywordRule.class, new RemoveUnnecessaryReturnReusableIntention());


        registerQuickFixes(UnnecessarySetterRule.class, new JavaStylePropertiesInvocationFixer());

        registerQuickFix(UnusedVariableRule.class, DeleteElementFix::new);
    }

    public static @NotNull LocalQuickFix[] getQuickFixesFor(@NotNull Violation violation, PsiElement element) {
        return Optional.ofNullable(MAPPINGS.get(violation.getRule().getClass()))
                .map(f -> f.apply(element))
                .map(l -> l.toArray(new LocalQuickFix[0]))
                .orElseGet(() -> {
                    LOG.warn("Quick fixes not configured for " + violation.getRule());
                    return new LocalQuickFix[0];
                });
    }

    private static void registerQuickFixes(Class<? extends AbstractRule> ruleType, LocalQuickFix... quickFixes) {
        MAPPINGS.put(ruleType, psi -> Arrays.asList(quickFixes));
    }

    private static void registerQuickFixes(Class<? extends AbstractRule> ruleType, Function<PsiElement, List<LocalQuickFix>> quickFixesFunction) {
        MAPPINGS.put(ruleType, quickFixesFunction);
    }

    private static void registerQuickFix(Class<? extends AbstractRule> ruleType, Function<PsiElement, LocalQuickFix> quickFixFunction) {
        registerQuickFixes(ruleType, quickFixFunction.andThen(Collections::singletonList));
    }

    private static void registerQuickFix(Class<? extends AbstractRule> ruleType, ReusableIntention intention) {
        registerQuickFixes(ruleType, IntentionQuickFix.from(intention));
    }

    private CodeNarcUiMappings() { }
}
