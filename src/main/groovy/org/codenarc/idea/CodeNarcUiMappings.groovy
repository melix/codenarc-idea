package org.codenarc.idea

import com.intellij.codeInspection.LocalQuickFix
import org.codenarc.rule.AbstractRule
import org.codenarc.rule.Violation
import org.codenarc.rule.basic.AssertWithinFinallyBlockRule
import org.codenarc.rule.basic.AssignmentInConditionalRule
import org.codenarc.rule.basic.BigDecimalInstantiationRule
import org.codenarc.rule.basic.BitwiseOperatorInConditionalRule
import org.codenarc.rule.basic.BooleanGetBooleanRule
import org.codenarc.rule.basic.BrokenNullCheckRule
import org.codenarc.rule.basic.BrokenOddnessCheckRule
import org.codenarc.rule.basic.ClassForNameRule
import org.codenarc.rule.basic.ComparisonOfTwoConstantsRule
import org.codenarc.rule.basic.ComparisonWithSelfRule
import org.codenarc.rule.basic.ConstantAssertExpressionRule
import org.codenarc.rule.basic.ConstantIfExpressionRule
import org.codenarc.rule.basic.ConstantTernaryExpressionRule
import org.codenarc.rule.basic.DeadCodeRule
import org.codenarc.rule.basic.DoubleNegativeRule
import org.codenarc.rule.basic.DuplicateCaseStatementRule
import org.codenarc.rule.basic.DuplicateMapKeyRule
import org.codenarc.rule.basic.DuplicateSetValueRule
import org.codenarc.rule.basic.EmptyCatchBlockRule
import org.codenarc.rule.basic.EmptyClassRule
import org.codenarc.rule.basic.EmptyElseBlockRule
import org.codenarc.rule.basic.EmptyFinallyBlockRule
import org.codenarc.rule.basic.EmptyForStatementRule
import org.codenarc.rule.basic.EmptyIfStatementRule
import org.codenarc.rule.basic.EmptyInstanceInitializerRule
import org.codenarc.rule.basic.EmptyMethodRule
import org.codenarc.rule.basic.EmptyStaticInitializerRule
import org.codenarc.rule.basic.EmptySwitchStatementRule
import org.codenarc.rule.basic.EmptySynchronizedStatementRule
import org.codenarc.rule.basic.EmptyTryBlockRule
import org.codenarc.rule.basic.EmptyWhileStatementRule
import org.codenarc.rule.basic.EqualsAndHashCodeRule
import org.codenarc.rule.basic.EqualsOverloadedRule
import org.codenarc.rule.basic.ExplicitGarbageCollectionRule
import org.codenarc.rule.basic.ForLoopShouldBeWhileLoopRule
import org.codenarc.rule.basic.HardCodedWindowsFileSeparatorRule
import org.codenarc.rule.basic.HardCodedWindowsRootDirectoryRule
import org.codenarc.rule.basic.IntegerGetIntegerRule
import org.codenarc.rule.basic.MultipleUnaryOperatorsRule
import org.codenarc.rule.basic.RandomDoubleCoercedToZeroRule
import org.codenarc.rule.basic.RemoveAllOnSelfRule
import org.codenarc.rule.basic.ReturnFromFinallyBlockRule
import org.codenarc.rule.basic.ThrowExceptionFromFinallyBlockRule
import org.codenarc.rule.braces.ElseBlockBracesRule
import org.codenarc.rule.braces.ForStatementBracesRule
import org.codenarc.rule.braces.IfStatementBracesRule
import org.codenarc.rule.braces.WhileStatementBracesRule
import org.codenarc.rule.comments.ClassJavadocRule
import org.codenarc.rule.comments.JavadocConsecutiveEmptyLinesRule
import org.codenarc.rule.comments.JavadocEmptyAuthorTagRule
import org.codenarc.rule.comments.JavadocEmptyExceptionTagRule
import org.codenarc.rule.comments.JavadocEmptyFirstLineRule
import org.codenarc.rule.comments.JavadocEmptyLastLineRule
import org.codenarc.rule.comments.JavadocEmptyParamTagRule
import org.codenarc.rule.comments.JavadocEmptyReturnTagRule
import org.codenarc.rule.comments.JavadocEmptySeeTagRule
import org.codenarc.rule.comments.JavadocEmptySinceTagRule
import org.codenarc.rule.comments.JavadocEmptyThrowsTagRule
import org.codenarc.rule.comments.JavadocEmptyVersionTagRule
import org.codenarc.rule.concurrency.BusyWaitRule
import org.codenarc.rule.concurrency.DoubleCheckedLockingRule
import org.codenarc.rule.concurrency.InconsistentPropertyLockingRule
import org.codenarc.rule.concurrency.InconsistentPropertySynchronizationRule
import org.codenarc.rule.concurrency.NestedSynchronizationRule
import org.codenarc.rule.concurrency.StaticCalendarFieldRule
import org.codenarc.rule.concurrency.StaticConnectionRule
import org.codenarc.rule.concurrency.StaticDateFormatFieldRule
import org.codenarc.rule.concurrency.StaticMatcherFieldRule
import org.codenarc.rule.concurrency.StaticSimpleDateFormatFieldRule
import org.codenarc.rule.concurrency.SynchronizedMethodRule
import org.codenarc.rule.concurrency.SynchronizedOnBoxedPrimitiveRule
import org.codenarc.rule.concurrency.SynchronizedOnGetClassRule
import org.codenarc.rule.concurrency.SynchronizedOnReentrantLockRule
import org.codenarc.rule.concurrency.SynchronizedOnStringRule
import org.codenarc.rule.concurrency.SynchronizedOnThisRule
import org.codenarc.rule.concurrency.SynchronizedReadObjectMethodRule
import org.codenarc.rule.concurrency.SystemRunFinalizersOnExitRule
import org.codenarc.rule.concurrency.ThisReferenceEscapesConstructorRule
import org.codenarc.rule.concurrency.ThreadGroupRule
import org.codenarc.rule.concurrency.ThreadLocalNotStaticFinalRule
import org.codenarc.rule.concurrency.ThreadYieldRule
import org.codenarc.rule.concurrency.UseOfNotifyMethodRule
import org.codenarc.rule.concurrency.VolatileArrayFieldRule
import org.codenarc.rule.concurrency.VolatileLongOrDoubleFieldRule
import org.codenarc.rule.concurrency.WaitOutsideOfWhileLoopRule
import org.codenarc.rule.convention.ConfusingTernaryRule
import org.codenarc.rule.convention.CouldBeElvisRule
import org.codenarc.rule.convention.CouldBeSwitchStatementRule
import org.codenarc.rule.convention.FieldTypeRequiredRule
import org.codenarc.rule.convention.HashtableIsObsoleteRule
import org.codenarc.rule.convention.IfStatementCouldBeTernaryRule
import org.codenarc.rule.convention.InvertedConditionRule
import org.codenarc.rule.convention.InvertedIfElseRule
import org.codenarc.rule.convention.LongLiteralWithLowerCaseLRule
import org.codenarc.rule.convention.MethodParameterTypeRequired
import org.codenarc.rule.convention.MethodReturnTypeRequiredRule
import org.codenarc.rule.convention.NoDefRule
import org.codenarc.rule.convention.NoJavaUtilDateRule
import org.codenarc.rule.convention.NoTabCharacterRule
import org.codenarc.rule.convention.ParameterReassignmentRule
import org.codenarc.rule.convention.PublicMethodsBeforeNonPublicMethodsRule
import org.codenarc.rule.convention.StaticFieldsBeforeInstanceFieldsRule
import org.codenarc.rule.convention.StaticMethodsBeforeInstanceMethodsRule
import org.codenarc.rule.convention.TernaryCouldBeElvisRule
import org.codenarc.rule.convention.TrailingCommaRule
import org.codenarc.rule.convention.VariableTypeRequiredRule
import org.codenarc.rule.convention.VectorIsObsoleteRule
import org.codenarc.rule.design.AbstractClassWithPublicConstructorRule
import org.codenarc.rule.design.AbstractClassWithoutAbstractMethodRule
import org.codenarc.rule.design.AssignmentToStaticFieldFromInstanceMethodRule
import org.codenarc.rule.design.BooleanMethodReturnsNullRule
import org.codenarc.rule.design.BuilderMethodWithSideEffectsRule
import org.codenarc.rule.design.CloneWithoutCloneableRule
import org.codenarc.rule.design.CloneableWithoutCloneRule
import org.codenarc.rule.design.CloseWithoutCloseableRule
import org.codenarc.rule.design.CompareToWithoutComparableRule
import org.codenarc.rule.design.ConstantsOnlyInterfaceRule
import org.codenarc.rule.design.EmptyMethodInAbstractClassRule
import org.codenarc.rule.design.FinalClassWithProtectedMemberRule
import org.codenarc.rule.design.ImplementationAsTypeRule
import org.codenarc.rule.design.InstanceofRule
import org.codenarc.rule.design.LocaleSetDefaultRule
import org.codenarc.rule.design.NestedForLoopRule
import org.codenarc.rule.design.PrivateFieldCouldBeFinalRule
import org.codenarc.rule.design.PublicInstanceFieldRule
import org.codenarc.rule.design.ReturnsNullInsteadOfEmptyArrayRule
import org.codenarc.rule.design.ReturnsNullInsteadOfEmptyCollectionRule
import org.codenarc.rule.design.SimpleDateFormatMissingLocaleRule
import org.codenarc.rule.design.StatelessSingletonRule
import org.codenarc.rule.design.ToStringReturnsNullRule
import org.codenarc.rule.dry.DuplicateListLiteralRule
import org.codenarc.rule.dry.DuplicateMapLiteralRule
import org.codenarc.rule.dry.DuplicateNumberLiteralRule
import org.codenarc.rule.dry.DuplicateStringLiteralRule
import org.codenarc.rule.exceptions.CatchArrayIndexOutOfBoundsExceptionRule
import org.codenarc.rule.exceptions.CatchErrorRule
import org.codenarc.rule.exceptions.CatchExceptionRule
import org.codenarc.rule.exceptions.CatchIllegalMonitorStateExceptionRule
import org.codenarc.rule.exceptions.CatchIndexOutOfBoundsExceptionRule
import org.codenarc.rule.exceptions.CatchNullPointerExceptionRule
import org.codenarc.rule.exceptions.CatchRuntimeExceptionRule
import org.codenarc.rule.exceptions.CatchThrowableRule
import org.codenarc.rule.exceptions.ConfusingClassNamedExceptionRule
import org.codenarc.rule.exceptions.ExceptionExtendsErrorRule
import org.codenarc.rule.exceptions.ExceptionExtendsThrowableRule
import org.codenarc.rule.exceptions.ExceptionNotThrownRule
import org.codenarc.rule.exceptions.MissingNewInThrowStatementRule
import org.codenarc.rule.exceptions.ReturnNullFromCatchBlockRule
import org.codenarc.rule.exceptions.SwallowThreadDeathRule
import org.codenarc.rule.exceptions.ThrowErrorRule
import org.codenarc.rule.exceptions.ThrowExceptionRule
import org.codenarc.rule.exceptions.ThrowNullPointerExceptionRule
import org.codenarc.rule.exceptions.ThrowRuntimeExceptionRule
import org.codenarc.rule.exceptions.ThrowThrowableRule
import org.codenarc.rule.formatting.BlankLineBeforePackageRule
import org.codenarc.rule.formatting.BlockEndsWithBlankLineRule
import org.codenarc.rule.formatting.BlockStartsWithBlankLineRule
import org.codenarc.rule.formatting.BracesForClassRule
import org.codenarc.rule.formatting.BracesForForLoopRule
import org.codenarc.rule.formatting.BracesForIfElseRule
import org.codenarc.rule.formatting.BracesForMethodRule
import org.codenarc.rule.formatting.BracesForTryCatchFinallyRule
import org.codenarc.rule.formatting.ClassEndsWithBlankLineRule
import org.codenarc.rule.formatting.ClassStartsWithBlankLineRule
import org.codenarc.rule.formatting.ClosureStatementOnOpeningLineOfMultipleLineClosureRule
import org.codenarc.rule.formatting.ConsecutiveBlankLinesRule
import org.codenarc.rule.formatting.FileEndsWithoutNewlineRule
import org.codenarc.rule.formatting.IndentationRule
import org.codenarc.rule.formatting.LineLengthRule
import org.codenarc.rule.formatting.MissingBlankLineAfterImportsRule
import org.codenarc.rule.formatting.MissingBlankLineAfterPackageRule
import org.codenarc.rule.formatting.SpaceAfterCatchRule
import org.codenarc.rule.formatting.SpaceAfterClosingBraceRule
import org.codenarc.rule.formatting.SpaceAfterCommaRule
import org.codenarc.rule.formatting.SpaceAfterForRule
import org.codenarc.rule.formatting.SpaceAfterIfRule
import org.codenarc.rule.formatting.SpaceAfterOpeningBraceRule
import org.codenarc.rule.formatting.SpaceAfterSemicolonRule
import org.codenarc.rule.formatting.SpaceAfterSwitchRule
import org.codenarc.rule.formatting.SpaceAfterWhileRule
import org.codenarc.rule.formatting.SpaceAroundClosureArrowRule
import org.codenarc.rule.formatting.SpaceAroundMapEntryColonRule
import org.codenarc.rule.formatting.SpaceAroundOperatorRule
import org.codenarc.rule.formatting.SpaceBeforeClosingBraceRule
import org.codenarc.rule.formatting.SpaceBeforeOpeningBraceRule
import org.codenarc.rule.formatting.TrailingWhitespaceRule
import org.codenarc.rule.generic.IllegalClassMemberRule
import org.codenarc.rule.generic.IllegalClassReferenceRule
import org.codenarc.rule.generic.IllegalPackageReferenceRule
import org.codenarc.rule.generic.IllegalRegexRule
import org.codenarc.rule.generic.IllegalStringRule
import org.codenarc.rule.generic.IllegalSubclassRule
import org.codenarc.rule.generic.RequiredRegexRule
import org.codenarc.rule.generic.RequiredStringRule
import org.codenarc.rule.generic.StatelessClassRule
import org.codenarc.rule.grails.GrailsDomainHasEqualsRule
import org.codenarc.rule.grails.GrailsDomainHasToStringRule
import org.codenarc.rule.grails.GrailsDomainReservedSqlKeywordNameRule
import org.codenarc.rule.grails.GrailsDomainStringPropertyMaxSizeRule
import org.codenarc.rule.grails.GrailsDomainWithServiceReferenceRule
import org.codenarc.rule.grails.GrailsDuplicateConstraintRule
import org.codenarc.rule.grails.GrailsDuplicateMappingRule
import org.codenarc.rule.grails.GrailsMassAssignmentRule
import org.codenarc.rule.grails.GrailsPublicControllerMethodRule
import org.codenarc.rule.grails.GrailsServletContextReferenceRule
import org.codenarc.rule.grails.GrailsStatelessServiceRule
import org.codenarc.rule.groovyism.AssignCollectionSortRule
import org.codenarc.rule.groovyism.AssignCollectionUniqueRule
import org.codenarc.rule.groovyism.ClosureAsLastMethodParameterRule
import org.codenarc.rule.groovyism.CollectAllIsDeprecatedRule
import org.codenarc.rule.groovyism.ConfusingMultipleReturnsRule
import org.codenarc.rule.groovyism.ExplicitArrayListInstantiationRule
import org.codenarc.rule.groovyism.ExplicitCallToAndMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToCompareToMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToDivMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToEqualsMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToGetAtMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToLeftShiftMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToMinusMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToModMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToMultiplyMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToOrMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToPlusMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToPowerMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToPutAtMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToRightShiftMethodRule
import org.codenarc.rule.groovyism.ExplicitCallToXorMethodRule
import org.codenarc.rule.groovyism.ExplicitHashMapInstantiationRule
import org.codenarc.rule.groovyism.ExplicitHashSetInstantiationRule
import org.codenarc.rule.groovyism.ExplicitLinkedHashMapInstantiationRule
import org.codenarc.rule.groovyism.ExplicitLinkedListInstantiationRule
import org.codenarc.rule.groovyism.ExplicitStackInstantiationRule
import org.codenarc.rule.groovyism.ExplicitTreeSetInstantiationRule
import org.codenarc.rule.groovyism.GStringAsMapKeyRule
import org.codenarc.rule.groovyism.GStringExpressionWithinStringRule
import org.codenarc.rule.groovyism.GetterMethodCouldBePropertyRule
import org.codenarc.rule.groovyism.GroovyLangImmutableRule
import org.codenarc.rule.groovyism.UseCollectManyRule
import org.codenarc.rule.groovyism.UseCollectNestedRule
import org.codenarc.rule.imports.DuplicateImportRule
import org.codenarc.rule.imports.ImportFromSamePackageRule
import org.codenarc.rule.imports.ImportFromSunPackagesRule
import org.codenarc.rule.imports.MisorderedStaticImportsRule
import org.codenarc.rule.imports.NoWildcardImportsRule
import org.codenarc.rule.imports.UnnecessaryGroovyImportRule
import org.codenarc.rule.imports.UnusedImportRule
import org.codenarc.rule.jdbc.DirectConnectionManagementRule
import org.codenarc.rule.jdbc.JdbcConnectionReferenceRule
import org.codenarc.rule.jdbc.JdbcResultSetReferenceRule
import org.codenarc.rule.jdbc.JdbcStatementReferenceRule
import org.codenarc.rule.junit.ChainedTestRule
import org.codenarc.rule.junit.CoupledTestCaseRule
import org.codenarc.rule.junit.JUnitAssertAlwaysFailsRule
import org.codenarc.rule.junit.JUnitAssertAlwaysSucceedsRule
import org.codenarc.rule.junit.JUnitAssertEqualsConstantActualValueRule
import org.codenarc.rule.junit.JUnitFailWithoutMessageRule
import org.codenarc.rule.junit.JUnitLostTestRule
import org.codenarc.rule.junit.JUnitPublicFieldRule
import org.codenarc.rule.junit.JUnitPublicNonTestMethodRule
import org.codenarc.rule.junit.JUnitPublicPropertyRule
import org.codenarc.rule.junit.JUnitSetUpCallsSuperRule
import org.codenarc.rule.junit.JUnitStyleAssertionsRule
import org.codenarc.rule.junit.JUnitTearDownCallsSuperRule
import org.codenarc.rule.junit.JUnitTestMethodWithoutAssertRule
import org.codenarc.rule.junit.JUnitUnnecessarySetUpRule
import org.codenarc.rule.junit.JUnitUnnecessaryTearDownRule
import org.codenarc.rule.junit.JUnitUnnecessaryThrowsExceptionRule
import org.codenarc.rule.junit.SpockIgnoreRestUsedRule
import org.codenarc.rule.junit.UnnecessaryFailRule
import org.codenarc.rule.junit.UseAssertEqualsInsteadOfAssertTrueRule
import org.codenarc.rule.junit.UseAssertFalseInsteadOfNegationRule
import org.codenarc.rule.junit.UseAssertNullInsteadOfAssertEqualsRule
import org.codenarc.rule.junit.UseAssertSameInsteadOfAssertTrueRule
import org.codenarc.rule.junit.UseAssertTrueInsteadOfAssertEqualsRule
import org.codenarc.rule.junit.UseAssertTrueInsteadOfNegationRule
import org.codenarc.rule.logging.LoggerForDifferentClassRule
import org.codenarc.rule.logging.LoggerWithWrongModifiersRule
import org.codenarc.rule.logging.LoggingSwallowsStacktraceRule
import org.codenarc.rule.logging.MultipleLoggersRule
import org.codenarc.rule.logging.PrintStackTraceRule
import org.codenarc.rule.logging.PrintlnRule
import org.codenarc.rule.logging.SystemErrPrintRule
import org.codenarc.rule.logging.SystemOutPrintRule
import org.codenarc.rule.naming.AbstractClassNameRule
import org.codenarc.rule.naming.ClassNameRule
import org.codenarc.rule.naming.ClassNameSameAsFilenameRule
import org.codenarc.rule.naming.ClassNameSameAsSuperclassRule
import org.codenarc.rule.naming.ConfusingMethodNameRule
import org.codenarc.rule.naming.FactoryMethodNameRule
import org.codenarc.rule.naming.FieldNameRule
import org.codenarc.rule.naming.InterfaceNameRule
import org.codenarc.rule.naming.InterfaceNameSameAsSuperInterfaceRule
import org.codenarc.rule.naming.MethodNameRule
import org.codenarc.rule.naming.ObjectOverrideMisspelledMethodNameRule
import org.codenarc.rule.naming.PackageNameMatchesFilePathRule
import org.codenarc.rule.naming.PackageNameRule
import org.codenarc.rule.naming.ParameterNameRule
import org.codenarc.rule.naming.PropertyNameRule
import org.codenarc.rule.naming.VariableNameRule
import org.codenarc.rule.security.FileCreateTempFileRule
import org.codenarc.rule.security.InsecureRandomRule
import org.codenarc.rule.security.JavaIoPackageAccessRule
import org.codenarc.rule.security.NonFinalPublicFieldRule
import org.codenarc.rule.security.NonFinalSubclassOfSensitiveInterfaceRule
import org.codenarc.rule.security.ObjectFinalizeRule
import org.codenarc.rule.security.PublicFinalizeMethodRule
import org.codenarc.rule.security.SystemExitRule
import org.codenarc.rule.security.UnsafeArrayDeclarationRule
import org.codenarc.rule.security.UnsafeImplementationAsMapRule
import org.codenarc.rule.serialization.EnumCustomSerializationIgnoredRule
import org.codenarc.rule.serialization.SerialPersistentFieldsRule
import org.codenarc.rule.serialization.SerialVersionUIDRule
import org.codenarc.rule.serialization.SerializableClassMustDefineSerialVersionUIDRule
import org.codenarc.rule.size.AbcMetricRule
import org.codenarc.rule.size.ClassSizeRule
import org.codenarc.rule.size.CrapMetricRule
import org.codenarc.rule.size.CyclomaticComplexityRule
import org.codenarc.rule.size.MethodCountRule
import org.codenarc.rule.size.MethodSizeRule
import org.codenarc.rule.size.NestedBlockDepthRule
import org.codenarc.rule.size.ParameterCountRule
import org.codenarc.rule.unnecessary.AddEmptyStringRule
import org.codenarc.rule.unnecessary.ConsecutiveLiteralAppendsRule
import org.codenarc.rule.unnecessary.ConsecutiveStringConcatenationRule
import org.codenarc.rule.unnecessary.UnnecessaryBigDecimalInstantiationRule
import org.codenarc.rule.unnecessary.UnnecessaryBigIntegerInstantiationRule
import org.codenarc.rule.unnecessary.UnnecessaryBooleanExpressionRule
import org.codenarc.rule.unnecessary.UnnecessaryBooleanInstantiationRule
import org.codenarc.rule.unnecessary.UnnecessaryCallForLastElementRule
import org.codenarc.rule.unnecessary.UnnecessaryCallToSubstringRule
import org.codenarc.rule.unnecessary.UnnecessaryCastRule
import org.codenarc.rule.unnecessary.UnnecessaryCatchBlockRule
import org.codenarc.rule.unnecessary.UnnecessaryCollectCallRule
import org.codenarc.rule.unnecessary.UnnecessaryCollectionCallRule
import org.codenarc.rule.unnecessary.UnnecessaryConstructorRule
import org.codenarc.rule.unnecessary.UnnecessaryDefInFieldDeclarationRule
import org.codenarc.rule.unnecessary.UnnecessaryDefInMethodDeclarationRule
import org.codenarc.rule.unnecessary.UnnecessaryDefInVariableDeclarationRule
import org.codenarc.rule.unnecessary.UnnecessaryDotClassRule
import org.codenarc.rule.unnecessary.UnnecessaryDoubleInstantiationRule
import org.codenarc.rule.unnecessary.UnnecessaryElseStatementRule
import org.codenarc.rule.unnecessary.UnnecessaryFinalOnPrivateMethodRule
import org.codenarc.rule.unnecessary.UnnecessaryFloatInstantiationRule
import org.codenarc.rule.unnecessary.UnnecessaryGStringRule
import org.codenarc.rule.unnecessary.UnnecessaryGetterRule
import org.codenarc.rule.unnecessary.UnnecessaryIfStatementRule
import org.codenarc.rule.unnecessary.UnnecessaryInstanceOfCheckRule
import org.codenarc.rule.unnecessary.UnnecessaryInstantiationToGetClassRule
import org.codenarc.rule.unnecessary.UnnecessaryIntegerInstantiationRule
import org.codenarc.rule.unnecessary.UnnecessaryLongInstantiationRule
import org.codenarc.rule.unnecessary.UnnecessaryModOneRule
import org.codenarc.rule.unnecessary.UnnecessaryNullCheckBeforeInstanceOfRule
import org.codenarc.rule.unnecessary.UnnecessaryNullCheckRule
import org.codenarc.rule.unnecessary.UnnecessaryObjectReferencesRule
import org.codenarc.rule.unnecessary.UnnecessaryOverridingMethodRule
import org.codenarc.rule.unnecessary.UnnecessaryPackageReferenceRule
import org.codenarc.rule.unnecessary.UnnecessaryParenthesesForMethodCallWithClosureRule
import org.codenarc.rule.unnecessary.UnnecessaryPublicModifierRule
import org.codenarc.rule.unnecessary.UnnecessaryReturnKeywordRule
import org.codenarc.rule.unnecessary.UnnecessarySafeNavigationOperatorRule
import org.codenarc.rule.unnecessary.UnnecessarySelfAssignmentRule
import org.codenarc.rule.unnecessary.UnnecessarySemicolonRule
import org.codenarc.rule.unnecessary.UnnecessarySetterRule
import org.codenarc.rule.unnecessary.UnnecessaryStringInstantiationRule
import org.codenarc.rule.unnecessary.UnnecessarySubstringRule
import org.codenarc.rule.unnecessary.UnnecessaryTernaryExpressionRule
import org.codenarc.rule.unnecessary.UnnecessaryToStringRule
import org.codenarc.rule.unnecessary.UnnecessaryTransientModifierRule
import org.codenarc.rule.unused.UnusedArrayRule
import org.codenarc.rule.unused.UnusedMethodParameterRule
import org.codenarc.rule.unused.UnusedObjectRule
import org.codenarc.rule.unused.UnusedPrivateFieldRule
import org.codenarc.rule.unused.UnusedPrivateMethodParameterRule
import org.codenarc.rule.unused.UnusedPrivateMethodRule
import org.codenarc.rule.unused.UnusedVariableRule
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.groovy.codeInspection.bugs.GrRemoveModifierFix
import org.jetbrains.plugins.groovy.codeInspection.control.GroovyConditionalCanBeElvisInspection
import org.jetbrains.plugins.groovy.codeInspection.naming.RenameFix
import org.jetbrains.plugins.groovy.lang.psi.api.auxiliary.modifiers.GrModifier

final class CodeNarcUiMappings {

    private static final Map<Class<? extends AbstractRule>, LocalQuickFix[]> mappings

    static {
        mappings = Collections.unmodifiableMap(
            [
                (AbcMetricRule)                                         : [],   // Requires the GMetrics jar
                (AbstractClassNameRule)                                 : [new RenameFix() ],
                (AbstractClassWithPublicConstructorRule)                : [new GrRemoveModifierFix(GrModifier.PUBLIC) ],
                (AbstractClassWithoutAbstractMethodRule)                : [],
                (AddEmptyStringRule)                                    : [],
                (AssertWithinFinallyBlockRule)                          : [],
                (AssignCollectionSortRule)                              : [],
                (AssignCollectionUniqueRule)                            : [],
                (AssignmentInConditionalRule)                           : [],
                (AssignmentToStaticFieldFromInstanceMethodRule)         : [],
                (BigDecimalInstantiationRule)                           : [],
                (BitwiseOperatorInConditionalRule)                      : [],
                (BlankLineBeforePackageRule)                            : [],
                (BlockEndsWithBlankLineRule)                            : [],
                (BlockStartsWithBlankLineRule)                          : [],
                (BooleanGetBooleanRule)                                 : [],
                (BooleanMethodReturnsNullRule)                          : [],
                (BracesForClassRule)                                    : [],
                (BracesForForLoopRule)                                  : [],
                (BracesForIfElseRule)                                   : [],
                (BracesForMethodRule)                                   : [],
                (BracesForTryCatchFinallyRule)                          : [],
                (BrokenNullCheckRule)                                   : [],
                (BrokenOddnessCheckRule)                                : [],
                (BuilderMethodWithSideEffectsRule)                      : [],
                (BusyWaitRule)                                          : [],
                (CatchArrayIndexOutOfBoundsExceptionRule)               : [],
                (CatchErrorRule)                                        : [],
                (CatchExceptionRule)                                    : [],
                (CatchIllegalMonitorStateExceptionRule)                 : [],
                (CatchIndexOutOfBoundsExceptionRule)                    : [],
                (CatchNullPointerExceptionRule)                         : [],
                (CatchRuntimeExceptionRule)                             : [],
                (CatchThrowableRule)                                    : [],
                (ChainedTestRule)                                       : [],
                (ClassEndsWithBlankLineRule)                            : [],
                (ClassForNameRule)                                      : [],
                (ClassJavadocRule)                                      : [],
                (ClassNameRule)                                         : [],
                (ClassNameSameAsFilenameRule)                           : [],
                (ClassNameSameAsSuperclassRule)                         : [],
                (ClassSizeRule)                                         : [],
                (ClassStartsWithBlankLineRule)                          : [],
                (CloneWithoutCloneableRule)                             : [],
                (CloneableWithoutCloneRule)                             : [],
                (CloseWithoutCloseableRule)                             : [],
                (ClosureAsLastMethodParameterRule)                      : [],
                (ClosureStatementOnOpeningLineOfMultipleLineClosureRule): [],
                (CollectAllIsDeprecatedRule)                            : [],
                (CompareToWithoutComparableRule)                        : [],
                (ComparisonOfTwoConstantsRule)                          : [],
                (ComparisonWithSelfRule)                                : [],
                (ConfusingClassNamedExceptionRule)                      : [],
                (ConfusingMethodNameRule)                               : [],
                (ConfusingMultipleReturnsRule)                          : [],
                (ConfusingTernaryRule)                                  : [],
                (ConsecutiveBlankLinesRule)                             : [],
                (ConsecutiveLiteralAppendsRule)                         : [],
                (ConsecutiveStringConcatenationRule)                    : [],
                (ConstantAssertExpressionRule)                          : [],
                (ConstantIfExpressionRule)                              : [],
                (ConstantTernaryExpressionRule)                         : [],
                (ConstantsOnlyInterfaceRule)                            : [],
                (CouldBeElvisRule)                                      : [GroovyConditionalCanBeElvisInspection ],
                (CouldBeSwitchStatementRule)                            : [],
                (CoupledTestCaseRule)                                   : [],
                (CrapMetricRule)                                        : [],   // Requires the GMetrics jar and a Cobertura coverage file
                (CyclomaticComplexityRule)                              : [],   // Requires the GMetrics jar
                (DeadCodeRule)                                          : [],
                (DirectConnectionManagementRule)                        : [],
                (DoubleCheckedLockingRule)                              : [],
                (DoubleNegativeRule)                                    : [],
                (DuplicateCaseStatementRule)                            : [],
                (DuplicateImportRule)                                   : [],
                (DuplicateListLiteralRule)                              : [],
                (DuplicateMapKeyRule)                                   : [],
                (DuplicateMapLiteralRule)                               : [],
                (DuplicateNumberLiteralRule)                            : [],
                (DuplicateSetValueRule)                                 : [],
                (DuplicateStringLiteralRule)                            : [],
                (ElseBlockBracesRule)                                   : [],
                (EmptyCatchBlockRule)                                   : [],
                (EmptyClassRule)                                        : [],
                (EmptyElseBlockRule)                                    : [],
                (EmptyFinallyBlockRule)                                 : [],
                (EmptyForStatementRule)                                 : [],
                (EmptyIfStatementRule)                                  : [],
                (EmptyInstanceInitializerRule)                          : [],
                (EmptyMethodRule)                                       : [],
                (EmptyMethodInAbstractClassRule)                        : [],
                (EmptyStaticInitializerRule)                            : [],
                (EmptySwitchStatementRule)                              : [],
                (EmptySynchronizedStatementRule)                        : [],
                (EmptyTryBlockRule)                                     : [],
                (EmptyWhileStatementRule)                               : [],
                (EnumCustomSerializationIgnoredRule)                    : [],
                (EqualsAndHashCodeRule)                                 : [],
                (EqualsOverloadedRule)                                  : [],
                (ExceptionExtendsErrorRule)                             : [],
                (ExceptionExtendsThrowableRule)                         : [],
                (ExceptionNotThrownRule)                                : [],
                (ExplicitArrayListInstantiationRule)                    : [],
                (ExplicitCallToAndMethodRule)                           : [],
                (ExplicitCallToCompareToMethodRule)                     : [],
                (ExplicitCallToDivMethodRule)                           : [],
                (ExplicitCallToEqualsMethodRule)                        : [],
                (ExplicitCallToGetAtMethodRule)                         : [],
                (ExplicitCallToLeftShiftMethodRule)                     : [],
                (ExplicitCallToMinusMethodRule)                         : [],
                (ExplicitCallToModMethodRule)                           : [],
                (ExplicitCallToMultiplyMethodRule)                      : [],
                (ExplicitCallToOrMethodRule)                            : [],
                (ExplicitCallToPlusMethodRule)                          : [],
                (ExplicitCallToPowerMethodRule)                         : [],
                (ExplicitCallToPutAtMethodRule)                         : [],
                (ExplicitCallToRightShiftMethodRule)                    : [],
                (ExplicitCallToXorMethodRule)                           : [],
                (ExplicitGarbageCollectionRule)                         : [],
                (ExplicitHashMapInstantiationRule)                      : [],
                (ExplicitHashSetInstantiationRule)                      : [],
                (ExplicitLinkedHashMapInstantiationRule)                : [],
                (ExplicitLinkedListInstantiationRule)                   : [],
                (ExplicitStackInstantiationRule)                        : [],
                (ExplicitTreeSetInstantiationRule)                      : [],
                (FactoryMethodNameRule)                                 : [],
                (FieldNameRule)                                         : [],
                (FieldTypeRequiredRule)                                 : [],
                (FileCreateTempFileRule)                                : [],
                (FileEndsWithoutNewlineRule)                            : [],
                (FinalClassWithProtectedMemberRule)                     : [],
                (ForLoopShouldBeWhileLoopRule)                          : [],
                (ForStatementBracesRule)                                : [],
                (GStringAsMapKeyRule)                                   : [],
                (GStringExpressionWithinStringRule)                     : [],
                (GetterMethodCouldBePropertyRule)                       : [],
                (GrailsDomainHasEqualsRule)                             : [],
                (GrailsDomainHasToStringRule)                           : [],
                (GrailsDomainReservedSqlKeywordNameRule)                : [],
                (GrailsDomainStringPropertyMaxSizeRule)                 : [],
                (GrailsDomainWithServiceReferenceRule)                  : [],
                (GrailsDuplicateConstraintRule)                         : [],
                (GrailsDuplicateMappingRule)                            : [],
                (GrailsMassAssignmentRule)                              : [],
                (GrailsPublicControllerMethodRule)                      : [],
                (GrailsServletContextReferenceRule)                     : [],
                (GrailsStatelessServiceRule)                            : [],
                (GroovyLangImmutableRule)                               : [],
                (HardCodedWindowsFileSeparatorRule)                     : [],
                (HardCodedWindowsRootDirectoryRule)                     : [],
                (HashtableIsObsoleteRule)                               : [],
                (IfStatementBracesRule)                                 : [],
                (IfStatementCouldBeTernaryRule)                         : [],
                (IllegalClassMemberRule)                                : [],
                (IllegalClassReferenceRule)                             : [],
                (IllegalPackageReferenceRule)                           : [],
                (IllegalRegexRule)                                      : [],
                (IllegalStringRule)                                     : [],
                (IllegalSubclassRule)                                   : [],
                (ImplementationAsTypeRule)                              : [],
                (ImportFromSamePackageRule)                             : [],
                (ImportFromSunPackagesRule)                             : [],
                (InconsistentPropertyLockingRule)                       : [],
                (InconsistentPropertySynchronizationRule)               : [],
                (IndentationRule)                                       : [],
                (InsecureRandomRule)                                    : [],
                (InstanceofRule)                                        : [],
                (IntegerGetIntegerRule)                                 : [],
                (InterfaceNameRule)                                     : [],
                (InterfaceNameSameAsSuperInterfaceRule)                 : [],
                (InvertedConditionRule)                                 : [],
                (InvertedIfElseRule)                                    : [],
                (JUnitAssertAlwaysFailsRule)                            : [],
                (JUnitAssertAlwaysSucceedsRule)                         : [],
                (JUnitAssertEqualsConstantActualValueRule)              : [],
                (JUnitFailWithoutMessageRule)                           : [],
                (JUnitLostTestRule)                                     : [],
                (JUnitPublicFieldRule)                                  : [],
                (JUnitPublicNonTestMethodRule)                          : [],
                (JUnitPublicPropertyRule)                               : [],
                (JUnitSetUpCallsSuperRule)                              : [],
                (JUnitStyleAssertionsRule)                              : [],
                (JUnitTearDownCallsSuperRule)                           : [],
                (JUnitTestMethodWithoutAssertRule)                      : [],
                (JUnitUnnecessarySetUpRule)                             : [],
                (JUnitUnnecessaryTearDownRule)                          : [],
                (JUnitUnnecessaryThrowsExceptionRule)                   : [],
                (JavaIoPackageAccessRule)                               : [],
                (JavadocConsecutiveEmptyLinesRule)                      : [],
                (JavadocEmptyAuthorTagRule)                             : [],
                (JavadocEmptyExceptionTagRule)                          : [],
                (JavadocEmptyFirstLineRule)                             : [],
                (JavadocEmptyLastLineRule)                              : [],
                (JavadocEmptyParamTagRule)                              : [],
                (JavadocEmptyReturnTagRule)                             : [],
                (JavadocEmptySeeTagRule)                                : [],
                (JavadocEmptySinceTagRule)                              : [],
                (JavadocEmptyThrowsTagRule)                             : [],
                (JavadocEmptyVersionTagRule)                            : [],
                (JdbcConnectionReferenceRule)                           : [],
                (JdbcResultSetReferenceRule)                            : [],
                (JdbcStatementReferenceRule)                            : [],
                (LineLengthRule)                                        : [],
                (LocaleSetDefaultRule)                                  : [],
                (LoggerForDifferentClassRule)                           : [],
                (LoggerWithWrongModifiersRule)                          : [],
                (LoggingSwallowsStacktraceRule)                         : [],
                (LongLiteralWithLowerCaseLRule)                         : [],
                (MethodCountRule)                                       : [],
                (MethodNameRule)                                        : [],
                (MethodParameterTypeRequired)                           : [],
                (MethodReturnTypeRequiredRule)                          : [],
                (MethodSizeRule)                                        : [],
                (MisorderedStaticImportsRule)                           : [],
                (MissingBlankLineAfterImportsRule)                      : [],
                (MissingBlankLineAfterPackageRule)                      : [],
                (MissingNewInThrowStatementRule)                        : [],
                (MultipleLoggersRule)                                   : [],
                (MultipleUnaryOperatorsRule)                            : [],
                (NestedBlockDepthRule)                                  : [],
                (NestedForLoopRule)                                     : [],
                (NestedSynchronizationRule)                             : [],
                (NoDefRule)                                             : [],
                (NoJavaUtilDateRule)                                    : [],
                (NoTabCharacterRule)                                    : [],
                (NoWildcardImportsRule)                                 : [],
                (NonFinalPublicFieldRule)                               : [],
                (NonFinalSubclassOfSensitiveInterfaceRule)              : [],
                (ObjectFinalizeRule)                                    : [],
                (ObjectOverrideMisspelledMethodNameRule)                : [],
                (PackageNameRule)                                       : [],
                (PackageNameMatchesFilePathRule)                        : [],
                (ParameterCountRule)                                    : [],
                (ParameterNameRule)                                     : [],
                (ParameterReassignmentRule)                             : [],
                (PrintStackTraceRule)                                   : [],
                (PrintlnRule)                                           : [],
                (PrivateFieldCouldBeFinalRule)                          : [],
                (PropertyNameRule)                                      : [],
                (PublicFinalizeMethodRule)                              : [],
                (PublicInstanceFieldRule)                               : [],
                (PublicMethodsBeforeNonPublicMethodsRule)               : [],
                (RandomDoubleCoercedToZeroRule)                         : [],
                (RemoveAllOnSelfRule)                                   : [],
                (RequiredRegexRule)                                     : [],
                (RequiredStringRule)                                    : [],
                (ReturnFromFinallyBlockRule)                            : [],
                (ReturnNullFromCatchBlockRule)                          : [],
                (ReturnsNullInsteadOfEmptyArrayRule)                    : [],
                (ReturnsNullInsteadOfEmptyCollectionRule)               : [],
                (SerialPersistentFieldsRule)                            : [],
                (SerialVersionUIDRule)                                  : [],
                (SerializableClassMustDefineSerialVersionUIDRule)       : [],
                (SimpleDateFormatMissingLocaleRule)                     : [],
                (SpaceAfterCatchRule)                                   : [],
                (SpaceAfterClosingBraceRule)                            : [],
                (SpaceAfterCommaRule)                                   : [],
                (SpaceAfterForRule)                                     : [],
                (SpaceAfterIfRule)                                      : [],
                (SpaceAfterOpeningBraceRule)                            : [],
                (SpaceAfterSemicolonRule)                               : [],
                (SpaceAfterSwitchRule)                                  : [],
                (SpaceAfterWhileRule)                                   : [],
                (SpaceAroundClosureArrowRule)                           : [],
                (SpaceAroundMapEntryColonRule)                          : [],
                (SpaceAroundOperatorRule)                               : [],
                (SpaceBeforeClosingBraceRule)                           : [],
                (SpaceBeforeOpeningBraceRule)                           : [],
                (SpockIgnoreRestUsedRule)                               : [],
                (StatelessClassRule)                                    : [],
                (StatelessSingletonRule)                                : [],
                (StaticCalendarFieldRule)                               : [],
                (StaticConnectionRule)                                  : [],
                (StaticDateFormatFieldRule)                             : [],
                (StaticFieldsBeforeInstanceFieldsRule)                  : [],
                (StaticMatcherFieldRule)                                : [],
                (StaticMethodsBeforeInstanceMethodsRule)                : [],
                (StaticSimpleDateFormatFieldRule)                       : [],
                (SwallowThreadDeathRule)                                : [],
                (SynchronizedMethodRule)                                : [],
                (SynchronizedOnBoxedPrimitiveRule)                      : [],
                (SynchronizedOnGetClassRule)                            : [],
                (SynchronizedOnReentrantLockRule)                       : [],
                (SynchronizedOnStringRule)                              : [],
                (SynchronizedOnThisRule)                                : [],
                (SynchronizedReadObjectMethodRule)                      : [],
                (SystemErrPrintRule)                                    : [],
                (SystemExitRule)                                        : [],
                (SystemOutPrintRule)                                    : [],
                (SystemRunFinalizersOnExitRule)                         : [],
                (TernaryCouldBeElvisRule)                               : [],
                (ThisReferenceEscapesConstructorRule)                   : [],
                (ThreadGroupRule)                                       : [],
                (ThreadLocalNotStaticFinalRule)                         : [],
                (ThreadYieldRule)                                       : [],
                (ThrowErrorRule)                                        : [],
                (ThrowExceptionRule)                                    : [],
                (ThrowExceptionFromFinallyBlockRule)                    : [],
                (ThrowNullPointerExceptionRule)                         : [],
                (ThrowRuntimeExceptionRule)                             : [],
                (ThrowThrowableRule)                                    : [],
                (ToStringReturnsNullRule)                               : [],
                (TrailingCommaRule)                                     : [],
                (TrailingWhitespaceRule)                                : [],
                (UnnecessaryBigDecimalInstantiationRule)                : [],
                (UnnecessaryBigIntegerInstantiationRule)                : [],
                (UnnecessaryBooleanExpressionRule)                      : [],
                (UnnecessaryBooleanInstantiationRule)                   : [],
                (UnnecessaryCallForLastElementRule)                     : [],
                (UnnecessaryCallToSubstringRule)                        : [],
                (UnnecessaryCastRule)                                   : [],
                (UnnecessaryCatchBlockRule)                             : [],
                (UnnecessaryCollectCallRule)                            : [],
                (UnnecessaryCollectionCallRule)                         : [],
                (UnnecessaryConstructorRule)                            : [],
                (UnnecessaryDefInFieldDeclarationRule)                  : [],
                (UnnecessaryDefInMethodDeclarationRule)                 : [],
                (UnnecessaryDefInVariableDeclarationRule)               : [],
                (UnnecessaryDotClassRule)                               : [],
                (UnnecessaryDoubleInstantiationRule)                    : [],
                (UnnecessaryElseStatementRule)                          : [],
                (UnnecessaryFailRule)                                   : [],
                (UnnecessaryFinalOnPrivateMethodRule)                   : [],
                (UnnecessaryFloatInstantiationRule)                     : [],
                (UnnecessaryGStringRule)                                : [],
                (UnnecessaryGetterRule)                                 : [],
                (UnnecessaryGroovyImportRule)                           : [],
                (UnnecessaryIfStatementRule)                            : [],
                (UnnecessaryInstanceOfCheckRule)                        : [],
                (UnnecessaryInstantiationToGetClassRule)                : [],
                (UnnecessaryIntegerInstantiationRule)                   : [],
                (UnnecessaryLongInstantiationRule)                      : [],
                (UnnecessaryModOneRule)                                 : [],
                (UnnecessaryNullCheckRule)                              : [],
                (UnnecessaryNullCheckBeforeInstanceOfRule)              : [],
                (UnnecessaryObjectReferencesRule)                       : [],
                (UnnecessaryOverridingMethodRule)                       : [],
                (UnnecessaryPackageReferenceRule)                       : [],
                (UnnecessaryParenthesesForMethodCallWithClosureRule)    : [],
                (UnnecessaryPublicModifierRule)                         : [],
                (UnnecessaryReturnKeywordRule)                          : [],
                (UnnecessarySafeNavigationOperatorRule)                 : [],
                (UnnecessarySelfAssignmentRule)                         : [],
                (UnnecessarySemicolonRule)                              : [],
                (UnnecessarySetterRule)                                 : [],
                (UnnecessaryStringInstantiationRule)                    : [],
                (UnnecessarySubstringRule)                              : [],
                (UnnecessaryTernaryExpressionRule)                      : [],
                (UnnecessaryToStringRule)                               : [],
                (UnnecessaryTransientModifierRule)                      : [],
                (UnsafeArrayDeclarationRule)                            : [],
                (UnsafeImplementationAsMapRule)                         : [],
                (UnusedArrayRule)                                       : [],
                (UnusedImportRule)                                      : [],
                (UnusedMethodParameterRule)                             : [],
                (UnusedObjectRule)                                      : [],
                (UnusedPrivateFieldRule)                                : [],
                (UnusedPrivateMethodRule)                               : [],
                (UnusedPrivateMethodParameterRule)                      : [],
                (UnusedVariableRule)                                    : [],
                (UseAssertEqualsInsteadOfAssertTrueRule)                : [],
                (UseAssertFalseInsteadOfNegationRule)                   : [],
                (UseAssertNullInsteadOfAssertEqualsRule)                : [],
                (UseAssertSameInsteadOfAssertTrueRule)                  : [],
                (UseAssertTrueInsteadOfAssertEqualsRule)                : [],
                (UseAssertTrueInsteadOfNegationRule)                    : [],
                (UseCollectManyRule)                                    : [],
                (UseCollectNestedRule)                                  : [],
                (UseOfNotifyMethodRule)                                 : [],
                (VariableNameRule)                                      : [],
                (VariableTypeRequiredRule)                              : [],
                (VectorIsObsoleteRule)                                  : [],
                (VolatileArrayFieldRule)                                : [],
                (VolatileLongOrDoubleFieldRule)                         : [],
                (WaitOutsideOfWhileLoopRule)                            : [],
                (WhileStatementBracesRule)                              : []
                ] as Map<Class<? extends AbstractRule>, LocalQuickFix[]>)
    }

    static @NotNull LocalQuickFix[] getQuickFixesFor(@NotNull Violation violation) {
        return mappings.get(violation.getRule().getClass())
    }
}
