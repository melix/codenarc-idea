package org.codenarc.idea;

import com.intellij.codeInsight.daemon.impl.quickfix.AddDefaultConstructorFix;
import com.intellij.codeInsight.daemon.impl.quickfix.DeleteElementFix;
import com.intellij.codeInsight.intention.AddAnnotationPsiFix;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.util.containers.JBIterable;
import groovy.transform.CompileDynamic;
import groovy.transform.CompileStatic;
import org.codenarc.idea.quickfix.ConvertGStringToStringReusableIntention;
import org.codenarc.idea.quickfix.IntentionQuickFix;
import org.codenarc.idea.quickfix.RemoveRedundantClassPropertyReusableIntention;
import org.codenarc.idea.quickfix.RemoveUnnecessaryReturnReusableIntention;
import org.codenarc.idea.quickfix.ReplacePrintlnWithAnnotationFix;
import org.codenarc.idea.quickfix.ReplaceStatementFix;
import org.codenarc.idea.quickfix.ReusableIntention;
import org.codenarc.rule.AbstractRule;
import org.codenarc.rule.Violation;
import org.codenarc.rule.basic.AssertWithinFinallyBlockRule;
import org.codenarc.rule.basic.AssignmentInConditionalRule;
import org.codenarc.rule.basic.BigDecimalInstantiationRule;
import org.codenarc.rule.basic.BitwiseOperatorInConditionalRule;
import org.codenarc.rule.basic.BooleanGetBooleanRule;
import org.codenarc.rule.basic.BrokenNullCheckRule;
import org.codenarc.rule.basic.BrokenOddnessCheckRule;
import org.codenarc.rule.basic.ClassForNameRule;
import org.codenarc.rule.basic.ComparisonOfTwoConstantsRule;
import org.codenarc.rule.basic.ComparisonWithSelfRule;
import org.codenarc.rule.basic.ConstantAssertExpressionRule;
import org.codenarc.rule.basic.ConstantIfExpressionRule;
import org.codenarc.rule.basic.ConstantTernaryExpressionRule;
import org.codenarc.rule.basic.DeadCodeRule;
import org.codenarc.rule.basic.DoubleNegativeRule;
import org.codenarc.rule.basic.DuplicateCaseStatementRule;
import org.codenarc.rule.basic.DuplicateMapKeyRule;
import org.codenarc.rule.basic.DuplicateSetValueRule;
import org.codenarc.rule.basic.EmptyCatchBlockRule;
import org.codenarc.rule.basic.EmptyClassRule;
import org.codenarc.rule.basic.EmptyElseBlockRule;
import org.codenarc.rule.basic.EmptyFinallyBlockRule;
import org.codenarc.rule.basic.EmptyForStatementRule;
import org.codenarc.rule.basic.EmptyIfStatementRule;
import org.codenarc.rule.basic.EmptyInstanceInitializerRule;
import org.codenarc.rule.basic.EmptyMethodRule;
import org.codenarc.rule.basic.EmptyStaticInitializerRule;
import org.codenarc.rule.basic.EmptySwitchStatementRule;
import org.codenarc.rule.basic.EmptySynchronizedStatementRule;
import org.codenarc.rule.basic.EmptyTryBlockRule;
import org.codenarc.rule.basic.EmptyWhileStatementRule;
import org.codenarc.rule.basic.EqualsAndHashCodeRule;
import org.codenarc.rule.basic.EqualsOverloadedRule;
import org.codenarc.rule.basic.ExplicitGarbageCollectionRule;
import org.codenarc.rule.basic.ForLoopShouldBeWhileLoopRule;
import org.codenarc.rule.basic.HardCodedWindowsFileSeparatorRule;
import org.codenarc.rule.basic.HardCodedWindowsRootDirectoryRule;
import org.codenarc.rule.basic.IntegerGetIntegerRule;
import org.codenarc.rule.basic.MultipleUnaryOperatorsRule;
import org.codenarc.rule.basic.RandomDoubleCoercedToZeroRule;
import org.codenarc.rule.basic.RemoveAllOnSelfRule;
import org.codenarc.rule.basic.ReturnFromFinallyBlockRule;
import org.codenarc.rule.basic.ThrowExceptionFromFinallyBlockRule;
import org.codenarc.rule.braces.ElseBlockBracesRule;
import org.codenarc.rule.braces.ForStatementBracesRule;
import org.codenarc.rule.braces.IfStatementBracesRule;
import org.codenarc.rule.braces.WhileStatementBracesRule;
import org.codenarc.rule.comments.ClassJavadocRule;
import org.codenarc.rule.comments.JavadocConsecutiveEmptyLinesRule;
import org.codenarc.rule.comments.JavadocEmptyAuthorTagRule;
import org.codenarc.rule.comments.JavadocEmptyExceptionTagRule;
import org.codenarc.rule.comments.JavadocEmptyFirstLineRule;
import org.codenarc.rule.comments.JavadocEmptyLastLineRule;
import org.codenarc.rule.comments.JavadocEmptyParamTagRule;
import org.codenarc.rule.comments.JavadocEmptyReturnTagRule;
import org.codenarc.rule.comments.JavadocEmptySeeTagRule;
import org.codenarc.rule.comments.JavadocEmptySinceTagRule;
import org.codenarc.rule.comments.JavadocEmptyThrowsTagRule;
import org.codenarc.rule.comments.JavadocEmptyVersionTagRule;
import org.codenarc.rule.comments.JavadocMissingThrowsDescriptionRule;
import org.codenarc.rule.concurrency.BusyWaitRule;
import org.codenarc.rule.concurrency.DoubleCheckedLockingRule;
import org.codenarc.rule.concurrency.InconsistentPropertyLockingRule;
import org.codenarc.rule.concurrency.InconsistentPropertySynchronizationRule;
import org.codenarc.rule.concurrency.NestedSynchronizationRule;
import org.codenarc.rule.concurrency.StaticCalendarFieldRule;
import org.codenarc.rule.concurrency.StaticConnectionRule;
import org.codenarc.rule.concurrency.StaticDateFormatFieldRule;
import org.codenarc.rule.concurrency.StaticMatcherFieldRule;
import org.codenarc.rule.concurrency.StaticSimpleDateFormatFieldRule;
import org.codenarc.rule.concurrency.SynchronizedMethodRule;
import org.codenarc.rule.concurrency.SynchronizedOnBoxedPrimitiveRule;
import org.codenarc.rule.concurrency.SynchronizedOnGetClassRule;
import org.codenarc.rule.concurrency.SynchronizedOnReentrantLockRule;
import org.codenarc.rule.concurrency.SynchronizedOnStringRule;
import org.codenarc.rule.concurrency.SynchronizedOnThisRule;
import org.codenarc.rule.concurrency.SynchronizedReadObjectMethodRule;
import org.codenarc.rule.concurrency.SystemRunFinalizersOnExitRule;
import org.codenarc.rule.concurrency.ThisReferenceEscapesConstructorRule;
import org.codenarc.rule.concurrency.ThreadGroupRule;
import org.codenarc.rule.concurrency.ThreadLocalNotStaticFinalRule;
import org.codenarc.rule.concurrency.ThreadYieldRule;
import org.codenarc.rule.concurrency.UseOfNotifyMethodRule;
import org.codenarc.rule.concurrency.VolatileArrayFieldRule;
import org.codenarc.rule.concurrency.VolatileLongOrDoubleFieldRule;
import org.codenarc.rule.concurrency.WaitOutsideOfWhileLoopRule;
import org.codenarc.rule.convention.CompileStaticRule;
import org.codenarc.rule.convention.ConfusingTernaryRule;
import org.codenarc.rule.convention.CouldBeElvisRule;
import org.codenarc.rule.convention.CouldBeSwitchStatementRule;
import org.codenarc.rule.convention.FieldTypeRequiredRule;
import org.codenarc.rule.convention.HashtableIsObsoleteRule;
import org.codenarc.rule.convention.IfStatementCouldBeTernaryRule;
import org.codenarc.rule.convention.ImplicitReturnStatementRule;
import org.codenarc.rule.convention.InvertedConditionRule;
import org.codenarc.rule.convention.InvertedIfElseRule;
import org.codenarc.rule.convention.LongLiteralWithLowerCaseLRule;
import org.codenarc.rule.convention.MethodParameterTypeRequired;
import org.codenarc.rule.convention.MethodReturnTypeRequiredRule;
import org.codenarc.rule.convention.NoDefRule;
import org.codenarc.rule.convention.NoJavaUtilDateRule;
import org.codenarc.rule.convention.NoTabCharacterRule;
import org.codenarc.rule.convention.ParameterReassignmentRule;
import org.codenarc.rule.convention.PublicMethodsBeforeNonPublicMethodsRule;
import org.codenarc.rule.convention.StaticFieldsBeforeInstanceFieldsRule;
import org.codenarc.rule.convention.StaticMethodsBeforeInstanceMethodsRule;
import org.codenarc.rule.convention.TernaryCouldBeElvisRule;
import org.codenarc.rule.convention.TrailingCommaRule;
import org.codenarc.rule.convention.VariableTypeRequiredRule;
import org.codenarc.rule.convention.VectorIsObsoleteRule;
import org.codenarc.rule.design.AbstractClassWithPublicConstructorRule;
import org.codenarc.rule.design.AbstractClassWithoutAbstractMethodRule;
import org.codenarc.rule.design.AssignmentToStaticFieldFromInstanceMethodRule;
import org.codenarc.rule.design.BooleanMethodReturnsNullRule;
import org.codenarc.rule.design.BuilderMethodWithSideEffectsRule;
import org.codenarc.rule.design.CloneWithoutCloneableRule;
import org.codenarc.rule.design.CloneableWithoutCloneRule;
import org.codenarc.rule.design.CloseWithoutCloseableRule;
import org.codenarc.rule.design.CompareToWithoutComparableRule;
import org.codenarc.rule.design.ConstantsOnlyInterfaceRule;
import org.codenarc.rule.design.EmptyMethodInAbstractClassRule;
import org.codenarc.rule.design.FinalClassWithProtectedMemberRule;
import org.codenarc.rule.design.ImplementationAsTypeRule;
import org.codenarc.rule.design.InstanceofRule;
import org.codenarc.rule.design.LocaleSetDefaultRule;
import org.codenarc.rule.design.NestedForLoopRule;
import org.codenarc.rule.design.PrivateFieldCouldBeFinalRule;
import org.codenarc.rule.design.PublicInstanceFieldRule;
import org.codenarc.rule.design.ReturnsNullInsteadOfEmptyArrayRule;
import org.codenarc.rule.design.ReturnsNullInsteadOfEmptyCollectionRule;
import org.codenarc.rule.design.SimpleDateFormatMissingLocaleRule;
import org.codenarc.rule.design.StatelessSingletonRule;
import org.codenarc.rule.design.ToStringReturnsNullRule;
import org.codenarc.rule.dry.DuplicateListLiteralRule;
import org.codenarc.rule.dry.DuplicateMapLiteralRule;
import org.codenarc.rule.dry.DuplicateNumberLiteralRule;
import org.codenarc.rule.dry.DuplicateStringLiteralRule;
import org.codenarc.rule.exceptions.CatchArrayIndexOutOfBoundsExceptionRule;
import org.codenarc.rule.exceptions.CatchErrorRule;
import org.codenarc.rule.exceptions.CatchExceptionRule;
import org.codenarc.rule.exceptions.CatchIllegalMonitorStateExceptionRule;
import org.codenarc.rule.exceptions.CatchIndexOutOfBoundsExceptionRule;
import org.codenarc.rule.exceptions.CatchNullPointerExceptionRule;
import org.codenarc.rule.exceptions.CatchRuntimeExceptionRule;
import org.codenarc.rule.exceptions.CatchThrowableRule;
import org.codenarc.rule.exceptions.ConfusingClassNamedExceptionRule;
import org.codenarc.rule.exceptions.ExceptionExtendsErrorRule;
import org.codenarc.rule.exceptions.ExceptionExtendsThrowableRule;
import org.codenarc.rule.exceptions.ExceptionNotThrownRule;
import org.codenarc.rule.exceptions.MissingNewInThrowStatementRule;
import org.codenarc.rule.exceptions.ReturnNullFromCatchBlockRule;
import org.codenarc.rule.exceptions.SwallowThreadDeathRule;
import org.codenarc.rule.exceptions.ThrowErrorRule;
import org.codenarc.rule.exceptions.ThrowExceptionRule;
import org.codenarc.rule.exceptions.ThrowNullPointerExceptionRule;
import org.codenarc.rule.exceptions.ThrowRuntimeExceptionRule;
import org.codenarc.rule.exceptions.ThrowThrowableRule;
import org.codenarc.rule.formatting.BlankLineBeforePackageRule;
import org.codenarc.rule.formatting.BlockEndsWithBlankLineRule;
import org.codenarc.rule.formatting.BlockStartsWithBlankLineRule;
import org.codenarc.rule.formatting.BracesForClassRule;
import org.codenarc.rule.formatting.BracesForForLoopRule;
import org.codenarc.rule.formatting.BracesForIfElseRule;
import org.codenarc.rule.formatting.BracesForMethodRule;
import org.codenarc.rule.formatting.BracesForTryCatchFinallyRule;
import org.codenarc.rule.formatting.ClassEndsWithBlankLineRule;
import org.codenarc.rule.formatting.ClassStartsWithBlankLineRule;
import org.codenarc.rule.formatting.ClosureStatementOnOpeningLineOfMultipleLineClosureRule;
import org.codenarc.rule.formatting.ConsecutiveBlankLinesRule;
import org.codenarc.rule.formatting.FileEndsWithoutNewlineRule;
import org.codenarc.rule.formatting.IndentationRule;
import org.codenarc.rule.formatting.LineLengthRule;
import org.codenarc.rule.formatting.MissingBlankLineAfterImportsRule;
import org.codenarc.rule.formatting.MissingBlankLineAfterPackageRule;
import org.codenarc.rule.formatting.SpaceAfterCatchRule;
import org.codenarc.rule.formatting.SpaceAfterClosingBraceRule;
import org.codenarc.rule.formatting.SpaceAfterCommaRule;
import org.codenarc.rule.formatting.SpaceAfterForRule;
import org.codenarc.rule.formatting.SpaceAfterIfRule;
import org.codenarc.rule.formatting.SpaceAfterOpeningBraceRule;
import org.codenarc.rule.formatting.SpaceAfterSemicolonRule;
import org.codenarc.rule.formatting.SpaceAfterSwitchRule;
import org.codenarc.rule.formatting.SpaceAfterWhileRule;
import org.codenarc.rule.formatting.SpaceAroundClosureArrowRule;
import org.codenarc.rule.formatting.SpaceAroundMapEntryColonRule;
import org.codenarc.rule.formatting.SpaceAroundOperatorRule;
import org.codenarc.rule.formatting.SpaceBeforeClosingBraceRule;
import org.codenarc.rule.formatting.SpaceBeforeOpeningBraceRule;
import org.codenarc.rule.formatting.TrailingWhitespaceRule;
import org.codenarc.rule.generic.IllegalClassMemberRule;
import org.codenarc.rule.generic.IllegalClassReferenceRule;
import org.codenarc.rule.generic.IllegalPackageReferenceRule;
import org.codenarc.rule.generic.IllegalRegexRule;
import org.codenarc.rule.generic.IllegalStringRule;
import org.codenarc.rule.generic.IllegalSubclassRule;
import org.codenarc.rule.generic.RequiredRegexRule;
import org.codenarc.rule.generic.RequiredStringRule;
import org.codenarc.rule.generic.StatelessClassRule;
import org.codenarc.rule.grails.GrailsDomainHasEqualsRule;
import org.codenarc.rule.grails.GrailsDomainHasToStringRule;
import org.codenarc.rule.grails.GrailsDomainReservedSqlKeywordNameRule;
import org.codenarc.rule.grails.GrailsDomainStringPropertyMaxSizeRule;
import org.codenarc.rule.grails.GrailsDomainWithServiceReferenceRule;
import org.codenarc.rule.grails.GrailsDuplicateConstraintRule;
import org.codenarc.rule.grails.GrailsDuplicateMappingRule;
import org.codenarc.rule.grails.GrailsMassAssignmentRule;
import org.codenarc.rule.grails.GrailsPublicControllerMethodRule;
import org.codenarc.rule.grails.GrailsServletContextReferenceRule;
import org.codenarc.rule.grails.GrailsStatelessServiceRule;
import org.codenarc.rule.groovyism.AssignCollectionSortRule;
import org.codenarc.rule.groovyism.AssignCollectionUniqueRule;
import org.codenarc.rule.groovyism.ClosureAsLastMethodParameterRule;
import org.codenarc.rule.groovyism.CollectAllIsDeprecatedRule;
import org.codenarc.rule.groovyism.ConfusingMultipleReturnsRule;
import org.codenarc.rule.groovyism.ExplicitArrayListInstantiationRule;
import org.codenarc.rule.groovyism.ExplicitCallToAndMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToCompareToMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToDivMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToEqualsMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToGetAtMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToLeftShiftMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToMinusMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToModMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToMultiplyMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToOrMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToPlusMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToPowerMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToPutAtMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToRightShiftMethodRule;
import org.codenarc.rule.groovyism.ExplicitCallToXorMethodRule;
import org.codenarc.rule.groovyism.ExplicitHashMapInstantiationRule;
import org.codenarc.rule.groovyism.ExplicitHashSetInstantiationRule;
import org.codenarc.rule.groovyism.ExplicitLinkedHashMapInstantiationRule;
import org.codenarc.rule.groovyism.ExplicitLinkedListInstantiationRule;
import org.codenarc.rule.groovyism.ExplicitStackInstantiationRule;
import org.codenarc.rule.groovyism.ExplicitTreeSetInstantiationRule;
import org.codenarc.rule.groovyism.GStringAsMapKeyRule;
import org.codenarc.rule.groovyism.GStringExpressionWithinStringRule;
import org.codenarc.rule.groovyism.GetterMethodCouldBePropertyRule;
import org.codenarc.rule.groovyism.GroovyLangImmutableRule;
import org.codenarc.rule.groovyism.UseCollectManyRule;
import org.codenarc.rule.groovyism.UseCollectNestedRule;
import org.codenarc.rule.imports.DuplicateImportRule;
import org.codenarc.rule.imports.ImportFromSamePackageRule;
import org.codenarc.rule.imports.ImportFromSunPackagesRule;
import org.codenarc.rule.imports.MisorderedStaticImportsRule;
import org.codenarc.rule.imports.NoWildcardImportsRule;
import org.codenarc.rule.imports.UnnecessaryGroovyImportRule;
import org.codenarc.rule.imports.UnusedImportRule;
import org.codenarc.rule.jdbc.DirectConnectionManagementRule;
import org.codenarc.rule.jdbc.JdbcConnectionReferenceRule;
import org.codenarc.rule.jdbc.JdbcResultSetReferenceRule;
import org.codenarc.rule.jdbc.JdbcStatementReferenceRule;
import org.codenarc.rule.junit.ChainedTestRule;
import org.codenarc.rule.junit.CoupledTestCaseRule;
import org.codenarc.rule.junit.JUnitAssertAlwaysFailsRule;
import org.codenarc.rule.junit.JUnitAssertAlwaysSucceedsRule;
import org.codenarc.rule.junit.JUnitAssertEqualsConstantActualValueRule;
import org.codenarc.rule.junit.JUnitFailWithoutMessageRule;
import org.codenarc.rule.junit.JUnitLostTestRule;
import org.codenarc.rule.junit.JUnitPublicFieldRule;
import org.codenarc.rule.junit.JUnitPublicNonTestMethodRule;
import org.codenarc.rule.junit.JUnitPublicPropertyRule;
import org.codenarc.rule.junit.JUnitSetUpCallsSuperRule;
import org.codenarc.rule.junit.JUnitStyleAssertionsRule;
import org.codenarc.rule.junit.JUnitTearDownCallsSuperRule;
import org.codenarc.rule.junit.JUnitTestMethodWithoutAssertRule;
import org.codenarc.rule.junit.JUnitUnnecessarySetUpRule;
import org.codenarc.rule.junit.JUnitUnnecessaryTearDownRule;
import org.codenarc.rule.junit.JUnitUnnecessaryThrowsExceptionRule;
import org.codenarc.rule.junit.SpockIgnoreRestUsedRule;
import org.codenarc.rule.junit.UnnecessaryFailRule;
import org.codenarc.rule.junit.UseAssertEqualsInsteadOfAssertTrueRule;
import org.codenarc.rule.junit.UseAssertFalseInsteadOfNegationRule;
import org.codenarc.rule.junit.UseAssertNullInsteadOfAssertEqualsRule;
import org.codenarc.rule.junit.UseAssertSameInsteadOfAssertTrueRule;
import org.codenarc.rule.junit.UseAssertTrueInsteadOfAssertEqualsRule;
import org.codenarc.rule.junit.UseAssertTrueInsteadOfNegationRule;
import org.codenarc.rule.logging.LoggerForDifferentClassRule;
import org.codenarc.rule.logging.LoggerWithWrongModifiersRule;
import org.codenarc.rule.logging.LoggingSwallowsStacktraceRule;
import org.codenarc.rule.logging.MultipleLoggersRule;
import org.codenarc.rule.logging.PrintStackTraceRule;
import org.codenarc.rule.logging.PrintlnRule;
import org.codenarc.rule.logging.SystemErrPrintRule;
import org.codenarc.rule.logging.SystemOutPrintRule;
import org.codenarc.rule.naming.AbstractClassNameRule;
import org.codenarc.rule.naming.ClassNameRule;
import org.codenarc.rule.naming.ClassNameSameAsFilenameRule;
import org.codenarc.rule.naming.ClassNameSameAsSuperclassRule;
import org.codenarc.rule.naming.ConfusingMethodNameRule;
import org.codenarc.rule.naming.FactoryMethodNameRule;
import org.codenarc.rule.naming.FieldNameRule;
import org.codenarc.rule.naming.InterfaceNameRule;
import org.codenarc.rule.naming.InterfaceNameSameAsSuperInterfaceRule;
import org.codenarc.rule.naming.MethodNameRule;
import org.codenarc.rule.naming.ObjectOverrideMisspelledMethodNameRule;
import org.codenarc.rule.naming.PackageNameMatchesFilePathRule;
import org.codenarc.rule.naming.PackageNameRule;
import org.codenarc.rule.naming.ParameterNameRule;
import org.codenarc.rule.naming.PropertyNameRule;
import org.codenarc.rule.naming.VariableNameRule;
import org.codenarc.rule.security.FileCreateTempFileRule;
import org.codenarc.rule.security.InsecureRandomRule;
import org.codenarc.rule.security.JavaIoPackageAccessRule;
import org.codenarc.rule.security.NonFinalPublicFieldRule;
import org.codenarc.rule.security.NonFinalSubclassOfSensitiveInterfaceRule;
import org.codenarc.rule.security.ObjectFinalizeRule;
import org.codenarc.rule.security.PublicFinalizeMethodRule;
import org.codenarc.rule.security.SystemExitRule;
import org.codenarc.rule.security.UnsafeArrayDeclarationRule;
import org.codenarc.rule.security.UnsafeImplementationAsMapRule;
import org.codenarc.rule.serialization.EnumCustomSerializationIgnoredRule;
import org.codenarc.rule.serialization.SerialPersistentFieldsRule;
import org.codenarc.rule.serialization.SerialVersionUIDRule;
import org.codenarc.rule.serialization.SerializableClassMustDefineSerialVersionUIDRule;
import org.codenarc.rule.size.AbcMetricRule;
import org.codenarc.rule.size.ClassSizeRule;
import org.codenarc.rule.size.CrapMetricRule;
import org.codenarc.rule.size.CyclomaticComplexityRule;
import org.codenarc.rule.size.MethodCountRule;
import org.codenarc.rule.size.MethodSizeRule;
import org.codenarc.rule.size.NestedBlockDepthRule;
import org.codenarc.rule.size.ParameterCountRule;
import org.codenarc.rule.unnecessary.AddEmptyStringRule;
import org.codenarc.rule.unnecessary.ConsecutiveLiteralAppendsRule;
import org.codenarc.rule.unnecessary.ConsecutiveStringConcatenationRule;
import org.codenarc.rule.unnecessary.UnnecessaryBigDecimalInstantiationRule;
import org.codenarc.rule.unnecessary.UnnecessaryBigIntegerInstantiationRule;
import org.codenarc.rule.unnecessary.UnnecessaryBooleanExpressionRule;
import org.codenarc.rule.unnecessary.UnnecessaryBooleanInstantiationRule;
import org.codenarc.rule.unnecessary.UnnecessaryCallForLastElementRule;
import org.codenarc.rule.unnecessary.UnnecessaryCallToSubstringRule;
import org.codenarc.rule.unnecessary.UnnecessaryCastRule;
import org.codenarc.rule.unnecessary.UnnecessaryCatchBlockRule;
import org.codenarc.rule.unnecessary.UnnecessaryCollectCallRule;
import org.codenarc.rule.unnecessary.UnnecessaryCollectionCallRule;
import org.codenarc.rule.unnecessary.UnnecessaryConstructorRule;
import org.codenarc.rule.unnecessary.UnnecessaryDefInFieldDeclarationRule;
import org.codenarc.rule.unnecessary.UnnecessaryDefInMethodDeclarationRule;
import org.codenarc.rule.unnecessary.UnnecessaryDefInVariableDeclarationRule;
import org.codenarc.rule.unnecessary.UnnecessaryDotClassRule;
import org.codenarc.rule.unnecessary.UnnecessaryDoubleInstantiationRule;
import org.codenarc.rule.unnecessary.UnnecessaryElseStatementRule;
import org.codenarc.rule.unnecessary.UnnecessaryFinalOnPrivateMethodRule;
import org.codenarc.rule.unnecessary.UnnecessaryFloatInstantiationRule;
import org.codenarc.rule.unnecessary.UnnecessaryGStringRule;
import org.codenarc.rule.unnecessary.UnnecessaryGetterRule;
import org.codenarc.rule.unnecessary.UnnecessaryIfStatementRule;
import org.codenarc.rule.unnecessary.UnnecessaryInstanceOfCheckRule;
import org.codenarc.rule.unnecessary.UnnecessaryInstantiationToGetClassRule;
import org.codenarc.rule.unnecessary.UnnecessaryIntegerInstantiationRule;
import org.codenarc.rule.unnecessary.UnnecessaryLongInstantiationRule;
import org.codenarc.rule.unnecessary.UnnecessaryModOneRule;
import org.codenarc.rule.unnecessary.UnnecessaryNullCheckBeforeInstanceOfRule;
import org.codenarc.rule.unnecessary.UnnecessaryNullCheckRule;
import org.codenarc.rule.unnecessary.UnnecessaryObjectReferencesRule;
import org.codenarc.rule.unnecessary.UnnecessaryOverridingMethodRule;
import org.codenarc.rule.unnecessary.UnnecessaryPackageReferenceRule;
import org.codenarc.rule.unnecessary.UnnecessaryParenthesesForMethodCallWithClosureRule;
import org.codenarc.rule.unnecessary.UnnecessaryPublicModifierRule;
import org.codenarc.rule.unnecessary.UnnecessaryReturnKeywordRule;
import org.codenarc.rule.unnecessary.UnnecessarySafeNavigationOperatorRule;
import org.codenarc.rule.unnecessary.UnnecessarySelfAssignmentRule;
import org.codenarc.rule.unnecessary.UnnecessarySemicolonRule;
import org.codenarc.rule.unnecessary.UnnecessarySetterRule;
import org.codenarc.rule.unnecessary.UnnecessaryStringInstantiationRule;
import org.codenarc.rule.unnecessary.UnnecessarySubstringRule;
import org.codenarc.rule.unnecessary.UnnecessaryTernaryExpressionRule;
import org.codenarc.rule.unnecessary.UnnecessaryToStringRule;
import org.codenarc.rule.unnecessary.UnnecessaryTransientModifierRule;
import org.codenarc.rule.unused.UnusedArrayRule;
import org.codenarc.rule.unused.UnusedMethodParameterRule;
import org.codenarc.rule.unused.UnusedObjectRule;
import org.codenarc.rule.unused.UnusedPrivateFieldRule;
import org.codenarc.rule.unused.UnusedPrivateMethodParameterRule;
import org.codenarc.rule.unused.UnusedPrivateMethodRule;
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
        registerQuickFixes(AbcMetricRule.class);   // Requires the GMetrics, no available action

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

        registerQuickFixes(AddEmptyStringRule.class); // TODO: implement fix

        registerQuickFixes(AssertWithinFinallyBlockRule.class); // TODO: implement fix

        registerQuickFixes(AssignCollectionSortRule.class, new ReplaceStatementFix(GrMethodCall.class, "sort()", "sort(false)"));

        registerQuickFixes(AssignCollectionUniqueRule.class, new ReplaceStatementFix(GrMethodCall.class, "unique()", "unique(false)"));

        registerQuickFixes(AssignmentInConditionalRule.class, new ReplaceStatementFix(GrAssignmentExpression.class, "=", "=="));

        // TODO: the following rules were not yet reviewed for their fixes (only randomly)
        registerQuickFixes(AssignmentToStaticFieldFromInstanceMethodRule.class);
        registerQuickFixes(BigDecimalInstantiationRule.class);
        registerQuickFixes(BitwiseOperatorInConditionalRule.class);

        registerQuickFix(BlankLineBeforePackageRule.class, DeleteElementFix::new);

        registerQuickFix(BlockEndsWithBlankLineRule.class, DeleteElementFix::new);

        registerQuickFix(BlockStartsWithBlankLineRule.class, DeleteElementFix::new);

        registerQuickFixes(BooleanGetBooleanRule.class);
        registerQuickFixes(BooleanMethodReturnsNullRule.class);
        registerQuickFixes(BracesForClassRule.class);
        registerQuickFixes(BracesForForLoopRule.class);
        registerQuickFixes(BracesForIfElseRule.class);
        registerQuickFixes(BracesForMethodRule.class);
        registerQuickFixes(BracesForTryCatchFinallyRule.class);
        registerQuickFixes(BrokenNullCheckRule.class);
        registerQuickFixes(BrokenOddnessCheckRule.class);
        registerQuickFixes(BuilderMethodWithSideEffectsRule.class);
        registerQuickFixes(BusyWaitRule.class);
        registerQuickFixes(CatchArrayIndexOutOfBoundsExceptionRule.class);
        registerQuickFixes(CatchErrorRule.class);
        registerQuickFixes(CatchExceptionRule.class);
        registerQuickFixes(CatchIllegalMonitorStateExceptionRule.class);
        registerQuickFixes(CatchIndexOutOfBoundsExceptionRule.class);
        registerQuickFixes(CatchNullPointerExceptionRule.class);
        registerQuickFixes(CatchRuntimeExceptionRule.class);
        registerQuickFixes(CatchThrowableRule.class);
        registerQuickFixes(ChainedTestRule.class);
        registerQuickFixes(ClassEndsWithBlankLineRule.class);
        registerQuickFixes(ClassForNameRule.class);
        registerQuickFixes(ClassJavadocRule.class);

        registerQuickFixes(ClassNameRule.class, new RenameFix());

        registerQuickFixes(ClassNameSameAsFilenameRule.class, new RenameFix());

        registerQuickFixes(ClassNameSameAsSuperclassRule.class, new RenameFix());

        registerQuickFixes(ClassSizeRule.class);
        registerQuickFixes(ClassStartsWithBlankLineRule.class);
        registerQuickFixes(CloneWithoutCloneableRule.class);
        registerQuickFixes(CloneableWithoutCloneRule.class);
        registerQuickFixes(CloseWithoutCloseableRule.class);
        registerQuickFixes(ClosureAsLastMethodParameterRule.class);
        registerQuickFixes(ClosureStatementOnOpeningLineOfMultipleLineClosureRule.class);
        registerQuickFixes(CollectAllIsDeprecatedRule.class);
        registerQuickFixes(CompareToWithoutComparableRule.class);
        registerQuickFixes(ComparisonOfTwoConstantsRule.class);
        registerQuickFixes(ComparisonWithSelfRule.class);

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

        registerQuickFixes(ConfusingMultipleReturnsRule.class);
        registerQuickFixes(ConfusingTernaryRule.class);

        registerQuickFix(ConsecutiveBlankLinesRule.class, DeleteElementFix::new);

        registerQuickFixes(ConsecutiveLiteralAppendsRule.class);
        registerQuickFixes(ConsecutiveStringConcatenationRule.class);
        registerQuickFixes(ConstantAssertExpressionRule.class);
        registerQuickFixes(ConstantIfExpressionRule.class);
        registerQuickFixes(ConstantTernaryExpressionRule.class);
        registerQuickFixes(ConstantsOnlyInterfaceRule.class);
        registerQuickFixes(CouldBeElvisRule.class); // can be elvis
        registerQuickFixes(CouldBeSwitchStatementRule.class);
        registerQuickFixes(CoupledTestCaseRule.class);
        registerQuickFixes(CrapMetricRule.class);   // Requires the GMetrics jar and a Cobertura coverage
        registerQuickFixes(CyclomaticComplexityRule.class);   // Requires the GMetrics

        registerQuickFix(DeadCodeRule.class, DeleteElementFix::new);

        registerQuickFixes(DirectConnectionManagementRule.class);
        registerQuickFixes(DoubleCheckedLockingRule.class);
        registerQuickFixes(DoubleNegativeRule.class);
        registerQuickFixes(DuplicateCaseStatementRule.class);
        registerQuickFixes(DuplicateImportRule.class);
        registerQuickFixes(DuplicateListLiteralRule.class);
        registerQuickFixes(DuplicateMapKeyRule.class);
        registerQuickFixes(DuplicateMapLiteralRule.class);
        registerQuickFixes(DuplicateNumberLiteralRule.class);
        registerQuickFixes(DuplicateSetValueRule.class);
        registerQuickFixes(DuplicateStringLiteralRule.class);
        registerQuickFixes(ElseBlockBracesRule.class);
        registerQuickFixes(EmptyCatchBlockRule.class);
        registerQuickFixes(EmptyClassRule.class);
        registerQuickFixes(EmptyElseBlockRule.class);
        registerQuickFixes(EmptyFinallyBlockRule.class);
        registerQuickFixes(EmptyForStatementRule.class);
        registerQuickFixes(EmptyIfStatementRule.class);
        registerQuickFixes(EmptyInstanceInitializerRule.class);
        registerQuickFixes(EmptyMethodRule.class);
        registerQuickFixes(EmptyMethodInAbstractClassRule.class);
        registerQuickFixes(EmptyStaticInitializerRule.class);
        registerQuickFixes(EmptySwitchStatementRule.class);
        registerQuickFixes(EmptySynchronizedStatementRule.class);
        registerQuickFixes(EmptyTryBlockRule.class);
        registerQuickFixes(EmptyWhileStatementRule.class);
        registerQuickFixes(EnumCustomSerializationIgnoredRule.class);
        registerQuickFixes(EqualsAndHashCodeRule.class);
        registerQuickFixes(EqualsOverloadedRule.class);
        registerQuickFixes(ExceptionExtendsErrorRule.class);
        registerQuickFixes(ExceptionExtendsThrowableRule.class);
        registerQuickFixes(ExceptionNotThrownRule.class);
        registerQuickFixes(ExplicitArrayListInstantiationRule.class);
        registerQuickFixes(ExplicitCallToAndMethodRule.class);
        registerQuickFixes(ExplicitCallToCompareToMethodRule.class);
        registerQuickFixes(ExplicitCallToDivMethodRule.class);
        registerQuickFixes(ExplicitCallToEqualsMethodRule.class);
        registerQuickFixes(ExplicitCallToGetAtMethodRule.class);
        registerQuickFixes(ExplicitCallToLeftShiftMethodRule.class);
        registerQuickFixes(ExplicitCallToMinusMethodRule.class);
        registerQuickFixes(ExplicitCallToModMethodRule.class);
        registerQuickFixes(ExplicitCallToMultiplyMethodRule.class);
        registerQuickFixes(ExplicitCallToOrMethodRule.class);
        registerQuickFixes(ExplicitCallToPlusMethodRule.class);
        registerQuickFixes(ExplicitCallToPowerMethodRule.class);
        registerQuickFixes(ExplicitCallToPutAtMethodRule.class);
        registerQuickFixes(ExplicitCallToRightShiftMethodRule.class);
        registerQuickFixes(ExplicitCallToXorMethodRule.class);
        registerQuickFixes(ExplicitGarbageCollectionRule.class);
        registerQuickFixes(ExplicitHashMapInstantiationRule.class);
        registerQuickFixes(ExplicitHashSetInstantiationRule.class);
        registerQuickFixes(ExplicitLinkedHashMapInstantiationRule.class);
        registerQuickFixes(ExplicitLinkedListInstantiationRule.class);
        registerQuickFixes(ExplicitStackInstantiationRule.class);
        registerQuickFixes(ExplicitTreeSetInstantiationRule.class);
        registerQuickFixes(FactoryMethodNameRule.class);
        registerQuickFixes(FieldNameRule.class);
        registerQuickFixes(FieldTypeRequiredRule.class);
        registerQuickFixes(FileCreateTempFileRule.class);
        registerQuickFixes(FileEndsWithoutNewlineRule.class);
        registerQuickFixes(FinalClassWithProtectedMemberRule.class);
        registerQuickFixes(ForLoopShouldBeWhileLoopRule.class);
        registerQuickFixes(ForStatementBracesRule.class);
        registerQuickFixes(GStringAsMapKeyRule.class);
        registerQuickFixes(GStringExpressionWithinStringRule.class);
        registerQuickFixes(GetterMethodCouldBePropertyRule.class);
        registerQuickFixes(GrailsDomainHasEqualsRule.class);
        registerQuickFixes(GrailsDomainHasToStringRule.class);
        registerQuickFixes(GrailsDomainReservedSqlKeywordNameRule.class);
        registerQuickFixes(GrailsDomainStringPropertyMaxSizeRule.class);
        registerQuickFixes(GrailsDomainWithServiceReferenceRule.class);
        registerQuickFixes(GrailsDuplicateConstraintRule.class);
        registerQuickFixes(GrailsDuplicateMappingRule.class);
        registerQuickFixes(GrailsMassAssignmentRule.class);
        registerQuickFixes(GrailsPublicControllerMethodRule.class);
        registerQuickFixes(GrailsServletContextReferenceRule.class);
        registerQuickFixes(GrailsStatelessServiceRule.class);
        registerQuickFixes(GroovyLangImmutableRule.class);
        registerQuickFixes(HardCodedWindowsFileSeparatorRule.class);
        registerQuickFixes(HardCodedWindowsRootDirectoryRule.class);
        registerQuickFixes(HashtableIsObsoleteRule.class);
        registerQuickFixes(IfStatementBracesRule.class);
        registerQuickFixes(IfStatementCouldBeTernaryRule.class);
        registerQuickFixes(IllegalClassMemberRule.class);
        registerQuickFixes(IllegalClassReferenceRule.class);
        registerQuickFixes(IllegalPackageReferenceRule.class);
        registerQuickFixes(IllegalRegexRule.class);
        registerQuickFixes(IllegalStringRule.class);
        registerQuickFixes(IllegalSubclassRule.class);
        registerQuickFixes(ImplementationAsTypeRule.class);
        registerQuickFixes(ImportFromSamePackageRule.class);
        registerQuickFixes(ImportFromSunPackagesRule.class);
        registerQuickFixes(InconsistentPropertyLockingRule.class);
        registerQuickFixes(InconsistentPropertySynchronizationRule.class);
        registerQuickFixes(IndentationRule.class);
        registerQuickFixes(InsecureRandomRule.class);
        registerQuickFixes(InstanceofRule.class);
        registerQuickFixes(IntegerGetIntegerRule.class);
        registerQuickFixes(InterfaceNameRule.class);
        registerQuickFixes(InterfaceNameSameAsSuperInterfaceRule.class);
        registerQuickFixes(InvertedConditionRule.class);
        registerQuickFixes(InvertedIfElseRule.class);
        registerQuickFixes(ImplicitReturnStatementRule.class);
        registerQuickFixes(JUnitAssertAlwaysFailsRule.class);
        registerQuickFixes(JUnitAssertAlwaysSucceedsRule.class);
        registerQuickFixes(JUnitAssertEqualsConstantActualValueRule.class);
        registerQuickFixes(JUnitFailWithoutMessageRule.class);
        registerQuickFixes(JUnitLostTestRule.class);
        registerQuickFixes(JUnitPublicFieldRule.class);
        registerQuickFixes(JUnitPublicNonTestMethodRule.class);
        registerQuickFixes(JUnitPublicPropertyRule.class);
        registerQuickFixes(JUnitSetUpCallsSuperRule.class);
        registerQuickFixes(JUnitStyleAssertionsRule.class);
        registerQuickFixes(JUnitTearDownCallsSuperRule.class);
        registerQuickFixes(JUnitTestMethodWithoutAssertRule.class);
        registerQuickFixes(JUnitUnnecessarySetUpRule.class);
        registerQuickFixes(JUnitUnnecessaryTearDownRule.class);
        registerQuickFixes(JUnitUnnecessaryThrowsExceptionRule.class);
        registerQuickFixes(JavaIoPackageAccessRule.class);
        registerQuickFixes(JavadocConsecutiveEmptyLinesRule.class);
        registerQuickFixes(JavadocEmptyAuthorTagRule.class);
        registerQuickFixes(JavadocEmptyExceptionTagRule.class);
        registerQuickFixes(JavadocEmptyFirstLineRule.class);
        registerQuickFixes(JavadocEmptyLastLineRule.class);
        registerQuickFixes(JavadocEmptyParamTagRule.class);
        registerQuickFixes(JavadocEmptyReturnTagRule.class);
        registerQuickFixes(JavadocEmptySeeTagRule.class);
        registerQuickFixes(JavadocEmptySinceTagRule.class);
        registerQuickFixes(JavadocEmptyThrowsTagRule.class);
        registerQuickFixes(JavadocEmptyVersionTagRule.class);
        registerQuickFixes(JavadocMissingThrowsDescriptionRule.class);
        registerQuickFixes(JdbcConnectionReferenceRule.class);
        registerQuickFixes(JdbcResultSetReferenceRule.class);
        registerQuickFixes(JdbcStatementReferenceRule.class);
        registerQuickFixes(LineLengthRule.class);
        registerQuickFixes(LocaleSetDefaultRule.class);
        registerQuickFixes(LoggerForDifferentClassRule.class);
        registerQuickFixes(LoggerWithWrongModifiersRule.class);
        registerQuickFixes(LoggingSwallowsStacktraceRule.class);
        registerQuickFixes(LongLiteralWithLowerCaseLRule.class);
        registerQuickFixes(MethodCountRule.class);
        registerQuickFixes(MethodNameRule.class);
        registerQuickFixes(MethodParameterTypeRequired.class);
        registerQuickFixes(MethodReturnTypeRequiredRule.class);
        registerQuickFixes(MethodSizeRule.class);
        registerQuickFixes(MisorderedStaticImportsRule.class);
        registerQuickFixes(MissingBlankLineAfterImportsRule.class);
        registerQuickFixes(MissingBlankLineAfterPackageRule.class);
        registerQuickFixes(MissingNewInThrowStatementRule.class);
        registerQuickFixes(MultipleLoggersRule.class);
        registerQuickFixes(MultipleUnaryOperatorsRule.class);
        registerQuickFixes(NestedBlockDepthRule.class);
        registerQuickFixes(NestedForLoopRule.class);
        registerQuickFixes(NestedSynchronizationRule.class);
        registerQuickFixes(NoDefRule.class);
        registerQuickFixes(NoJavaUtilDateRule.class);
        registerQuickFixes(NoTabCharacterRule.class);
        registerQuickFixes(NoWildcardImportsRule.class);
        registerQuickFixes(NonFinalPublicFieldRule.class);
        registerQuickFixes(NonFinalSubclassOfSensitiveInterfaceRule.class);
        registerQuickFixes(ObjectFinalizeRule.class);
        registerQuickFixes(ObjectOverrideMisspelledMethodNameRule.class);
        registerQuickFixes(PackageNameRule.class);
        registerQuickFixes(PackageNameMatchesFilePathRule.class);
        registerQuickFixes(ParameterCountRule.class);
        registerQuickFixes(ParameterNameRule.class);
        registerQuickFixes(ParameterReassignmentRule.class);
        registerQuickFixes(PrintStackTraceRule.class);

        registerQuickFixes(PrintlnRule.class, new ReplacePrintlnWithAnnotationFix());

        registerQuickFixes(PrivateFieldCouldBeFinalRule.class);
        registerQuickFixes(PropertyNameRule.class);
        registerQuickFixes(PublicFinalizeMethodRule.class);
        registerQuickFixes(PublicInstanceFieldRule.class);
        registerQuickFixes(PublicMethodsBeforeNonPublicMethodsRule.class);
        registerQuickFixes(RandomDoubleCoercedToZeroRule.class);
        registerQuickFixes(RemoveAllOnSelfRule.class);
        registerQuickFixes(RequiredRegexRule.class);
        registerQuickFixes(RequiredStringRule.class);
        registerQuickFixes(ReturnFromFinallyBlockRule.class);
        registerQuickFixes(ReturnNullFromCatchBlockRule.class);
        registerQuickFixes(ReturnsNullInsteadOfEmptyArrayRule.class);
        registerQuickFixes(ReturnsNullInsteadOfEmptyCollectionRule.class);
        registerQuickFixes(SerialPersistentFieldsRule.class);
        registerQuickFixes(SerialVersionUIDRule.class);
        registerQuickFixes(SerializableClassMustDefineSerialVersionUIDRule.class);
        registerQuickFixes(SimpleDateFormatMissingLocaleRule.class);
        registerQuickFixes(SpaceAfterCatchRule.class);
        registerQuickFixes(SpaceAfterClosingBraceRule.class);
        registerQuickFixes(SpaceAfterCommaRule.class);
        registerQuickFixes(SpaceAfterForRule.class);
        registerQuickFixes(SpaceAfterIfRule.class);
        registerQuickFixes(SpaceAfterOpeningBraceRule.class);
        registerQuickFixes(SpaceAfterSemicolonRule.class);
        registerQuickFixes(SpaceAfterSwitchRule.class);
        registerQuickFixes(SpaceAfterWhileRule.class);
        registerQuickFixes(SpaceAroundClosureArrowRule.class);
        registerQuickFixes(SpaceAroundMapEntryColonRule.class);
        registerQuickFixes(SpaceAroundOperatorRule.class);
        registerQuickFixes(SpaceBeforeClosingBraceRule.class);
        registerQuickFixes(SpaceBeforeOpeningBraceRule.class);
        registerQuickFixes(SpockIgnoreRestUsedRule.class);
        registerQuickFixes(StatelessClassRule.class);
        registerQuickFixes(StatelessSingletonRule.class);
        registerQuickFixes(StaticCalendarFieldRule.class);
        registerQuickFixes(StaticConnectionRule.class);
        registerQuickFixes(StaticDateFormatFieldRule.class);
        registerQuickFixes(StaticFieldsBeforeInstanceFieldsRule.class);
        registerQuickFixes(StaticMatcherFieldRule.class);
        registerQuickFixes(StaticMethodsBeforeInstanceMethodsRule.class);
        registerQuickFixes(StaticSimpleDateFormatFieldRule.class);
        registerQuickFixes(SwallowThreadDeathRule.class);
        registerQuickFixes(SynchronizedMethodRule.class);
        registerQuickFixes(SynchronizedOnBoxedPrimitiveRule.class);
        registerQuickFixes(SynchronizedOnGetClassRule.class);
        registerQuickFixes(SynchronizedOnReentrantLockRule.class);
        registerQuickFixes(SynchronizedOnStringRule.class);
        registerQuickFixes(SynchronizedOnThisRule.class);
        registerQuickFixes(SynchronizedReadObjectMethodRule.class);
        registerQuickFixes(SystemErrPrintRule.class);
        registerQuickFixes(SystemExitRule.class);
        registerQuickFixes(SystemOutPrintRule.class);
        registerQuickFixes(SystemRunFinalizersOnExitRule.class);
        registerQuickFixes(TernaryCouldBeElvisRule.class);
        registerQuickFixes(ThisReferenceEscapesConstructorRule.class);
        registerQuickFixes(ThreadGroupRule.class);
        registerQuickFixes(ThreadLocalNotStaticFinalRule.class);
        registerQuickFixes(ThreadYieldRule.class);
        registerQuickFixes(ThrowErrorRule.class);
        registerQuickFixes(ThrowExceptionRule.class);
        registerQuickFixes(ThrowExceptionFromFinallyBlockRule.class);
        registerQuickFixes(ThrowNullPointerExceptionRule.class);
        registerQuickFixes(ThrowRuntimeExceptionRule.class);
        registerQuickFixes(ThrowThrowableRule.class);
        registerQuickFixes(ToStringReturnsNullRule.class);
        registerQuickFixes(TrailingCommaRule.class);
        registerQuickFixes(TrailingWhitespaceRule.class);
        registerQuickFixes(UnnecessaryBigDecimalInstantiationRule.class);
        registerQuickFixes(UnnecessaryBigIntegerInstantiationRule.class);
        registerQuickFixes(UnnecessaryBooleanExpressionRule.class);
        registerQuickFixes(UnnecessaryBooleanInstantiationRule.class);
        registerQuickFixes(UnnecessaryCallForLastElementRule.class);
        registerQuickFixes(UnnecessaryCallToSubstringRule.class);
        registerQuickFixes(UnnecessaryCastRule.class);
        registerQuickFixes(UnnecessaryCatchBlockRule.class);
        registerQuickFixes(UnnecessaryCollectCallRule.class);
        registerQuickFixes(UnnecessaryCollectionCallRule.class);
        registerQuickFixes(UnnecessaryConstructorRule.class);
        registerQuickFixes(UnnecessaryDefInFieldDeclarationRule.class);
        registerQuickFixes(UnnecessaryDefInMethodDeclarationRule.class);
        registerQuickFixes(UnnecessaryDefInVariableDeclarationRule.class);

        registerQuickFix(UnnecessaryDotClassRule.class, new RemoveRedundantClassPropertyReusableIntention());

        registerQuickFixes(UnnecessaryDoubleInstantiationRule.class);
        registerQuickFixes(UnnecessaryElseStatementRule.class);
        registerQuickFixes(UnnecessaryFailRule.class);
        registerQuickFixes(UnnecessaryFinalOnPrivateMethodRule.class);
        registerQuickFixes(UnnecessaryFloatInstantiationRule.class);

        registerQuickFix(UnnecessaryGStringRule.class, new ConvertGStringToStringReusableIntention());

        registerQuickFixes(UnnecessaryGetterRule.class, new JavaStylePropertiesInvocationFixer());

        registerQuickFixes(UnnecessaryGroovyImportRule.class);
        registerQuickFixes(UnnecessaryIfStatementRule.class);
        registerQuickFixes(UnnecessaryInstanceOfCheckRule.class);
        registerQuickFixes(UnnecessaryInstantiationToGetClassRule.class);
        registerQuickFixes(UnnecessaryIntegerInstantiationRule.class);
        registerQuickFixes(UnnecessaryLongInstantiationRule.class);
        registerQuickFixes(UnnecessaryModOneRule.class);
        registerQuickFixes(UnnecessaryNullCheckRule.class);
        registerQuickFixes(UnnecessaryNullCheckBeforeInstanceOfRule.class);
        registerQuickFixes(UnnecessaryObjectReferencesRule.class);
        registerQuickFixes(UnnecessaryOverridingMethodRule.class);
        registerQuickFixes(UnnecessaryPackageReferenceRule.class);
        registerQuickFixes(UnnecessaryParenthesesForMethodCallWithClosureRule.class);
        registerQuickFixes(UnnecessaryPublicModifierRule.class);

        registerQuickFix(UnnecessaryReturnKeywordRule.class, new RemoveUnnecessaryReturnReusableIntention());

        registerQuickFixes(UnnecessarySafeNavigationOperatorRule.class);
        registerQuickFixes(UnnecessarySelfAssignmentRule.class);
        registerQuickFixes(UnnecessarySemicolonRule.class);

        registerQuickFixes(UnnecessarySetterRule.class, new JavaStylePropertiesInvocationFixer());

        registerQuickFixes(UnnecessaryStringInstantiationRule.class);
        registerQuickFixes(UnnecessarySubstringRule.class);
        registerQuickFixes(UnnecessaryTernaryExpressionRule.class);
        registerQuickFixes(UnnecessaryToStringRule.class);
        registerQuickFixes(UnnecessaryTransientModifierRule.class);
        registerQuickFixes(UnsafeArrayDeclarationRule.class);
        registerQuickFixes(UnsafeImplementationAsMapRule.class);
        registerQuickFixes(UnusedArrayRule.class);
        registerQuickFixes(UnusedImportRule.class);
        registerQuickFixes(UnusedMethodParameterRule.class);
        registerQuickFixes(UnusedObjectRule.class);
        registerQuickFixes(UnusedPrivateFieldRule.class);
        registerQuickFixes(UnusedPrivateMethodRule.class);
        registerQuickFixes(UnusedPrivateMethodParameterRule.class);

        registerQuickFix(UnusedVariableRule.class, DeleteElementFix::new);

        registerQuickFixes(UseAssertEqualsInsteadOfAssertTrueRule.class);
        registerQuickFixes(UseAssertFalseInsteadOfNegationRule.class);
        registerQuickFixes(UseAssertNullInsteadOfAssertEqualsRule.class);
        registerQuickFixes(UseAssertSameInsteadOfAssertTrueRule.class);
        registerQuickFixes(UseAssertTrueInsteadOfAssertEqualsRule.class);
        registerQuickFixes(UseAssertTrueInsteadOfNegationRule.class);
        registerQuickFixes(UseCollectManyRule.class);
        registerQuickFixes(UseCollectNestedRule.class);
        registerQuickFixes(UseOfNotifyMethodRule.class);
        registerQuickFixes(VariableNameRule.class);
        registerQuickFixes(VariableTypeRequiredRule.class);
        registerQuickFixes(VectorIsObsoleteRule.class);
        registerQuickFixes(VolatileArrayFieldRule.class);
        registerQuickFixes(VolatileLongOrDoubleFieldRule.class);
        registerQuickFixes(WaitOutsideOfWhileLoopRule.class);
        registerQuickFixes(WhileStatementBracesRule.class);
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

    private static void registerQuickFixes(Class<? extends AbstractRule> ruleType) {
        MAPPINGS.put(ruleType, psi -> Collections.emptyList());
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
