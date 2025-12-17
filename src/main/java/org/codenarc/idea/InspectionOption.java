package org.codenarc.idea;

import com.intellij.codeInspection.options.OptRegularComponent;
import com.intellij.codeInspection.options.StringValidator;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaProperty;
import org.codenarc.rule.AbstractRule;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

import static com.intellij.codeInspection.options.OptPane.*;
import static com.intellij.codeInspection.options.OptPane.string;

@NullMarked
final class InspectionOption<R extends AbstractRule> {
    private static final StringValidator CHAR_VALIDATOR = StringValidator.of(
        "charValidator",
        str -> {
            if (str.isEmpty()) {
                return "The value cannot be empty";
            }

            if (str.length() > 1) {
                return "The value must contain only one character";
            }

            return null;
        }
    );

    private static final StringValidator BIG_DECIMAL_VALIDATOR = StringValidator.of(
        "bigDecimalValidator",
        str -> {
            if (str.isEmpty()) {
                return "The value cannot be empty";
            }

            try {
                new BigDecimal(str);
            } catch (NumberFormatException e) {
                return "The value must be a valid BigDecimal number";
            }

            return null;
        }
    );

    private final R rule;
    private final MetaProperty metaProperty;
    private final Type type;
    private final String name;
    private final String label;

    private InspectionOption(R rule, MetaBeanProperty metaProperty, Type type, String name, String label) {
        this.rule = rule;
        this.metaProperty = metaProperty;
        this.type = type;
        this.name = name;
        this.label = label;
    }

    static <R extends AbstractRule> InspectionOption<R> from(R rule, MetaBeanProperty metaProperty) {
        return new InspectionOption<>(
            rule,
            metaProperty,
            Type.from(metaProperty.getType()),
            metaProperty.getName(),
            Utils.camelCaseToSentence(metaProperty.getName())
        );
    }

    OptRegularComponent getOptRegularComponent() {
        return switch (type) {
            case BOOLEAN -> checkbox(name, label);
            case INTEGER -> number(name, label, Integer.MIN_VALUE, Integer.MAX_VALUE);
            case BIG_DECIMAL -> string(name, label, BIG_DECIMAL_VALIDATOR);
            case STRING -> string(name, label);
            case CHAR -> string(name, label, 1, CHAR_VALIDATOR);
            case UNSUPPORTED -> throw new IllegalStateException(
                "Unsupported option \"" +name + "\" type=" + type
            );
        };
    }

    Object getValue() {
        return convertGetValue(metaProperty.getProperty(rule));
    }

    void setValue(Object value) {
        metaProperty.setProperty(rule, convertSetValue(value));
    }

    private Object convertGetValue(@Nullable Object value) {
        return switch (type) {
            case BOOLEAN -> Objects.requireNonNullElse(value, false);
            case INTEGER -> Objects.requireNonNullElse(value, 0);
            case BIG_DECIMAL -> Objects.requireNonNullElse(value, BigDecimal.ZERO).toString();
            case STRING -> Objects.requireNonNullElse(value, "");
            case CHAR -> Objects.requireNonNullElse(value, "").toString();
            case UNSUPPORTED -> throw new IllegalStateException("Unsupported option type " + type);
        };
    }

    private Object convertSetValue(Object value) {
        return switch (type) {
            case BOOLEAN, INTEGER, STRING -> value;
            case BIG_DECIMAL -> {
                var stringValue = (String) value;
                yield BIG_DECIMAL_VALIDATOR.getErrorMessage(null, stringValue) == null
                    ? new BigDecimal(stringValue)
                    : BigDecimal.ZERO;
            }
            case CHAR -> {
                var stringValue = (String) value;
                yield CHAR_VALIDATOR.getErrorMessage(null, stringValue) == null
                    ? stringValue.charAt(0)
                    : '\0';
            }
            case UNSUPPORTED -> throw new IllegalStateException("Unsupported option type " + type);
        };
    }

    private enum Type {
        BOOLEAN,
        INTEGER,
        BIG_DECIMAL,
        STRING,
        CHAR,
        UNSUPPORTED;

        private static Type from(Class<?> type) {
            if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                return BOOLEAN;
            }

            if (Integer.class.equals(type) || int.class.equals(type)) {
                return INTEGER;
            }

            if (BigDecimal.class.equals(type)) {
                return BIG_DECIMAL;
            }

            if (String.class.equals(type)) {
                return STRING;
            }

            if (Character.class.equals(type) || char.class.equals(type)) {
                return CHAR;
            }

            return UNSUPPORTED;
        }
    }
}
