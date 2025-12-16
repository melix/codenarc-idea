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
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.ClassUtil;
import com.intellij.util.IncorrectOperationException;
import com.siyeh.IntentionPowerPackBundle;
import org.jetbrains.plugins.groovy.codeInspection.GroovyFix;
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElement;
import org.jetbrains.plugins.groovy.lang.psi.GroovyPsiElementFactory;
import org.jetbrains.plugins.groovy.lang.psi.GroovyRecursiveElementVisitor;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.GrTypeDefinition;
import org.jetbrains.plugins.groovy.lang.psi.api.toplevel.imports.GrImportStatement;
import org.jetbrains.plugins.groovy.lang.psi.api.types.GrCodeReferenceElement;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@NullMarked
public class ReplaceOnDemandImportFix extends GroovyFix {
    @Override
    protected void doFix(Project project, ProblemDescriptor descriptor) throws IncorrectOperationException {
        if (descriptor.getPsiElement() instanceof GrImportStatement) {
            fixImport(project, (GrImportStatement) descriptor.getPsiElement());
        }
    }

    @Override
    public String getFamilyName() {
        return IntentionPowerPackBundle.message("replace.on.demand.import.intention.family.name");
    }

    protected void fixImport(Project project, GrImportStatement importStatement) {
        final GroovyFile groovyFile = (GroovyFile) importStatement.getContainingFile();
        final GroovyPsiElementFactory factory = GroovyPsiElementFactory.getInstance(project);

        if (!importStatement.isStatic()) {
            final GrTypeDefinition[] classes = (GrTypeDefinition[]) groovyFile.getClasses();
            final String qualifiedName = importStatement.getImportFqn();
            if (qualifiedName == null) {
                return;
            }

            final ClassCollector visitor = new ClassCollector(qualifiedName);
            for (GrTypeDefinition aClass : classes) {
                aClass.accept(visitor);
            }
            final PsiClass[] importedClasses = visitor.getImportedClasses();
            Arrays.sort(importedClasses, new PsiClassComparator());
            createImportStatements(importStatement, importedClasses, factory);
        }
    }

    private static void createImportStatements(
        GrImportStatement importStatement,
        PsiClass[] importedMembers,
        GroovyPsiElementFactory factory
    ) {
        final GroovyFile groovyFile = (GroovyFile) importStatement.getParent();
        for (PsiClass importedMember : importedMembers) {
            var qualifiedName = importedMember.getQualifiedName();
            if (qualifiedName != null) {
                groovyFile.addImport(factory.createImportStatement(qualifiedName, false, false, null, null));
            }
        }
        PsiElement maybeNewLine = importStatement.getPrevSibling();
        if (maybeNewLine != null && maybeNewLine.toString().contains("new line")) {
            maybeNewLine.delete();
        }
        importStatement.delete();
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
            if (element instanceof GrCodeReferenceElement ref) {
                if (ref.isQualified()) {
                    return;
                }
                final PsiElement resolvedElement = ref.resolve();
                if (!(resolvedElement instanceof PsiClass aClass)) {
                    return;
                }
                final String qualifiedName = aClass.getQualifiedName();
                final String packageName =
                    ClassUtil.extractPackageName(qualifiedName);
                if (!importedPackageName.equals(packageName)) {
                    return;
                }
                importedClasses.add(aClass);
            }
        }

        private PsiClass[] getImportedClasses() {
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

            if (qualifiedName2 == null) {
                return 1;
            }

            return qualifiedName1.compareTo(qualifiedName2);
        }
    }
}