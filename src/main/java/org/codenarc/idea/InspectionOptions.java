package org.codenarc.idea;

import com.intellij.codeInspection.options.OptPane;
import com.intellij.codeInspection.options.OptionContainer;
import com.intellij.codeInspection.options.OptionController;
import com.intellij.util.containers.ContainerUtil;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaProperty;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codenarc.rule.AbstractRule;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@NullMarked
final class InspectionOptions<R extends AbstractRule> implements OptionContainer {
    private static final List<String> EXCLUDED_RULE_STATIC_FIELDS = List.of(
        "name", "description", "priority", "astVisitor", "astVisitorClass", "violationMessage", "compilerPhase",
        "class", "enabled"
    );

    private final R rule;

    private @Nullable Map<String, InspectionOption<R>> options;

    private InspectionOptions(R rule) {
        this.rule = rule;
    }

    static <R extends AbstractRule> InspectionOptions<R> from(R rule) {
        return new InspectionOptions<>(rule);
    }

    @Override
    public OptPane getOptionsPane() {
        return new OptPane(
            ContainerUtil.map(getOptions().values(), InspectionOption::getOptRegularComponent)
        );
    }

    @Override
    public OptionController getOptionController() {
        return OptionController.of(
            (name) ->getOption(name).getValue(),
            (name, value) -> getOption(name).setValue(value)
        );
    }

    private InspectionOption<R> getOption(String name) {
        return Objects.requireNonNull(getOptions().get(name));
    }

    private Map<String, InspectionOption<R>> getOptions() {
        if (options == null) {
            options = DefaultGroovyMethods.getMetaClass(rule).getProperties().stream()
                .filter(property ->
                    property instanceof MetaBeanProperty p &&
                        !EXCLUDED_RULE_STATIC_FIELDS.contains(p.getName()) &&
                        p.getSetter() != null &&
                        p.getGetter() != null
                )
                .collect(Collectors.toMap(
                    MetaProperty::getName,
                    property -> InspectionOption.from(rule, (MetaBeanProperty) property),
                    (existing, replacement) -> existing,
                    TreeMap::new
                ));
        }

        return options;
    }
}
