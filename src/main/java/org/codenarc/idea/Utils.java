package org.codenarc.idea;

import org.jspecify.annotations.NullMarked;

@NullMarked
final class Utils {
    static String camelCaseToSentence(String camelCased) {
        if (camelCased.isBlank()) {
            return camelCased;
        }

        var buf = new StringBuilder(camelCased);
        buf.setCharAt(0, Character.toUpperCase(buf.charAt(0)));
        for (int i = 1; i < buf.length() - 1; i++) {
            if (
                Character.isLowerCase(buf.charAt(i - 1)) &&
                    Character.isUpperCase(buf.charAt(i)) &&
                    Character.isLowerCase(buf.charAt(i + 1))
            ) {
                buf.insert(i++, " ");
                buf.setCharAt(i, Character.toLowerCase(buf.charAt(i)));
            }
        }

        return buf.toString();
    }
}
