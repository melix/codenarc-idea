package org.codenarc.idea.disablerules;

import com.intellij.psi.PsiFile;
import org.codenarc.rule.Rule;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class DisabledRulesService {
    private static DisabledRulesService instance;

    private static final Map<String, CachedDisabledRulesLookupTable> cache = new LinkedHashMap<>();

    public static DisabledRulesService getInstance() {
        if (instance == null) {
            instance = new DisabledRulesService();
        }

        return instance;
    }

    private DisabledRulesService() {}

    public boolean isRuleDisabled(@NotNull Rule rule, @NotNull PsiFile file, Integer lineNumber) {
        if (lineNumber == null) {
            lineNumber = 1;
        }

        return getDisabledRulesLookupTable(file).isRuleDisabledForLine(rule, lineNumber);
    }

    private DisabledRulesLookupTable getDisabledRulesLookupTable(@NotNull PsiFile file) {
        var virtualFile = file.getVirtualFile();
        var cacheKey = virtualFile != null ? virtualFile.getCanonicalPath() : file.getName();

        var cacheValue = cache.get(cacheKey);
        if (cacheValue == null || cacheValue.modificationStamp != file.getModificationStamp()) {
            cacheValue = new CachedDisabledRulesLookupTable(
                new DisabledRulesLookupTable(file.getText()),
                file.getModificationStamp()
            );
            cache.put(cacheKey, cacheValue);
        }

        return cacheValue.disabledRulesLookupTable;
    }

    private record CachedDisabledRulesLookupTable(
        DisabledRulesLookupTable disabledRulesLookupTable,
        long modificationStamp
    ) { }
}
