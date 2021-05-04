/*
 * Copyright 2006-2018 Bas Leijdekkers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.idea.quickfix;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.ClassUtil;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.IntentionPowerPackBundle;
import com.siyeh.ig.psiutils.CommentTracker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElement;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory;
import org.jetbrains.plugins.groovy.lang.psi.GroovyRecursiveElementVisitor;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.GrTypeDefinition;
import org.jetbrains.plugins.groovy.lang.psi.api.toplevel.imports.GrImportStatement;
import org.jetbrains.plugins.groovy.lang.psi.api.types.GrCodeReferenceElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class ReplaceOnDemandImportFix extends GroovyFix {

    @Override
    protected void doFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) throws IncorrectOperationException {
        if (descriptor.getPsiElement() instanceof GrImportStatement) {
            fixImport(project, (GrImportStatement) descriptor.getPsiElement());
        }
    }

    @Override
    public @NotNull String getFamilyName() {
        return IntentionPowerPackBundle.message("replace.on.demand.import.intention.family.name");
    }

    protected void fixImport(@NotNull Project project, @NotNull GrImportStatement importStatement) {
        final GroovyFile groovyFile = (GroovyFile) importStatement.getContainingFile();
        final GroovyPsiElementFactory factory = GroovyPsiElementFactory.getInstance(project);

        if (!importStatement.isStatic()) {
            final GrTypeDefinition[] classes = (GrTypeDefinition[]) groovyFile.getClasses();
            final String qualifiedName = importStatement.getImportFqn();
            final ClassCollector visitor = new ClassCollector(qualifiedName);
            for (GrTypeDefinition aClass : classes) {
                aClass.accept(visitor);
            }
            final PsiClass[] importedClasses = visitor.getImportedClasses();
            Arrays.sort(importedClasses, new PsiClassComparator());
            createImportStatements(
                    importStatement,
                    importedClasses,
                    clazz -> factory.createImportStatement(clazz.getQualifiedName(), false, false, null, null)
            );
        } else {
            PsiClass targetClass = importStatement.resolveTargetClass();
            if (targetClass != null) {
                String[] members = collectReferencesThrough(
                        groovyFile,
                        importStatement.getImportReference(),
                        importStatement
                )
                        .stream()
                        .map(PsiReference::resolve)
                        .filter(resolve -> resolve instanceof PsiMember)
                        .map(member -> ((PsiMember) member).getName())
                        .distinct()
                        .filter(Objects::nonNull)
                        .toArray(String[]::new);

                createImportStatements(importStatement,
                        members,
                        member -> factory.createImportStatement(member, true, false, null, null));
            }
        }
    }

    private static <T> void createImportStatements(
            GrImportStatement importStatement,
            T[] importedMembers,
            Function<? super T, ? extends GrImportStatement> function
    ) {
        final GroovyFile groovyFile = (GroovyFile) importStatement.getParent();
        for (T importedMember : importedMembers) {
            groovyFile.addImport(function.apply(importedMember));
        }
        new CommentTracker().deleteAndRestoreComments(importStatement);
    }

    private static List<PsiJavaCodeReferenceElement> collectReferencesThrough(
            PsiFile file,
            @Nullable final GrCodeReferenceElement refExpr,
            final GrImportStatement staticImport
    ) {
        final List<PsiJavaCodeReferenceElement> expressionToExpand = new ArrayList<>();
        file.accept(new JavaRecursiveElementWalkingVisitor() {
            @Override
            public void visitReferenceElement(PsiJavaCodeReferenceElement expression) {
                if (refExpr == null || refExpr != expression) {
                    final PsiElement resolveScope = expression.advancedResolve(true).getCurrentFileResolveScope();
                    if (resolveScope == staticImport) {
                        expressionToExpand.add(expression);
                    }
                }
                super.visitElement(expression);
            }
        });
        return expressionToExpand;
    }

    private static class ClassCollector extends GroovyRecursiveElementVisitor {

        private final String importedPackageName;
        private final Set<PsiClass> importedClasses = new HashSet<>();

        ClassCollector(String importedPackageName) {
            this.importedPackageName = importedPackageName;
        }

        @Override
        public void visitElement(GroovyPsiElement element) {
            super.visitElement(element);
            if (element instanceof GrCodeReferenceElement) {
                GrCodeReferenceElement ref = (GrCodeReferenceElement) element;
                if (ref.isQualified()) {
                    return;
                }
                final PsiElement resolvedElement = ref.resolve();
                if (!(resolvedElement instanceof PsiClass)) {
                    return;
                }
                final PsiClass aClass = (PsiClass) resolvedElement;
                final String qualifiedName = aClass.getQualifiedName();
                final String packageName =
                        ClassUtil.extractPackageName(qualifiedName);
                if (!importedPackageName.equals(packageName)) {
                    return;
                }
                importedClasses.add(aClass);
            }
        }

        public PsiClass[] getImportedClasses() {
            return importedClasses.toArray(PsiClass.EMPTY_ARRAY);
        }
    }

    private static final class PsiClassComparator
            implements Comparator<PsiClass> {

        @Override
        public int compare(PsiClass class1, PsiClass class2) {
            final String qualifiedName1 = class1.getQualifiedName();
            final String qualifiedName2 = class2.getQualifiedName();
            if (qualifiedName1 == null) {
                return -1;
            }
            return qualifiedName1.compareTo(qualifiedName2);
        }
    }
}
