package org.codenarc.idea.disablerules;

import org.codenarc.rule.Rule;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class DisabledRulesLookupTable {
    protected static final String ALL_RULES = "#ALL#";
    private static final String CODENARC_DISABLE = "codenarc-disable";
    private static final String CODENARC_DISABLE_LINE = "codenarc-disable-line";
    private static final String CODENARC_ENABLE = "codenarc-enable";
    private static final String CODENARC_DESCRIPTION_START = "--";
    private static final Set<String> EMPTY = Collections.emptySet();

    private final Map<Integer, Set<String>> disabledRulesByLine = new LinkedHashMap<>();
    private final Set<String> currentlyDisabledRuleNames = new HashSet<>();
    private final Set<String> thisLineDisabledRuleNames = new HashSet<>();

    private boolean isDisablingAllRules = false;

    DisabledRulesLookupTable(@NotNull String sourceCode) {
        buildLookupTable(sourceCode);
    }

    boolean isRuleDisabledForLine(@NotNull Rule rule, int lineNumber) {
        var disabledRules = disabledRulesByLine.get(lineNumber);
        return disabledRules != null && (disabledRules.contains(ALL_RULES) || disabledRules.contains(rule.getName()));
    }

    private void buildLookupTable(@NotNull String sourceCode) {
        var lineNumber = new AtomicInteger(1);
        sourceCode.lines().forEach(line -> {
            checkForCodeNarcDisable(line);
            checkForCodeNarcEnable(line);
            setDisabledRulesByLine(lineNumber.getAndIncrement());
        });
    }

    private void checkForCodeNarcDisable(String line) {
        thisLineDisabledRuleNames.clear();
        if (line.contains(CODENARC_DISABLE_LINE)) {
            var ruleNames = parseRuleNames(line, CODENARC_DISABLE_LINE);
            if (!ruleNames.isEmpty()) {
                thisLineDisabledRuleNames.addAll(ruleNames);
            } else {
                thisLineDisabledRuleNames.add(ALL_RULES);
            }
        } else if (line.contains(CODENARC_DISABLE)) {
            var ruleNames = parseRuleNames(line, CODENARC_DISABLE);
            if (!ruleNames.isEmpty()) {
                currentlyDisabledRuleNames.addAll(ruleNames);
            } else {
                isDisablingAllRules = true;
            }
        }
    }

    private void checkForCodeNarcEnable(String line) {
        if (line.contains(CODENARC_ENABLE)) {
            var ruleNames = parseRuleNames(line, CODENARC_ENABLE);
            if (!ruleNames.isEmpty()) {
                currentlyDisabledRuleNames.removeAll(ruleNames);
            } else {
                currentlyDisabledRuleNames.clear();
                isDisablingAllRules = false;
            }
        }
    }

    private void setDisabledRulesByLine(int lineNumber) {
        if (isDisablingAllRules) {
            disabledRulesByLine.put(lineNumber, Set.of(ALL_RULES));
        } else {
            var disabled = new HashSet<String>();
            disabled.addAll(thisLineDisabledRuleNames);
            disabled.addAll(currentlyDisabledRuleNames);
            disabledRulesByLine.put(lineNumber, disabled);
        }
    }

    private static Set<String> parseRuleNames(String line, String codeNarcToken) {
        int index = line.indexOf(codeNarcToken);
        if (index == -1) {
            return EMPTY;
        }

        int startIndex = index + codeNarcToken.length();
        String rawRestOfLine = line.substring(startIndex);
        String restOfLine = rawRestOfLine.trim().replaceAll("\\*/", "");
        int descriptionIndex = restOfLine.indexOf(CODENARC_DESCRIPTION_START);
        String rawNames = descriptionIndex > -1 ? restOfLine.substring(0, descriptionIndex) : restOfLine;
        return Arrays.stream(rawNames.trim().split("\\s*,\\s*"))
            .filter(name -> !name.isEmpty()).collect(Collectors.toSet());
    }
}
