package org.codenarc.idea.disablerules;

import com.intellij.psi.PsiFile;
import org.codenarc.rule.Rule;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

@NullMarked
public class DisabledRulesService {
    private static @Nullable DisabledRulesService instance;
    private static final Map<String, CachedDisabledRulesLookupTable> cache = new LinkedHashMap<>();

    public static DisabledRulesService getInstance() {
        if (instance == null) {
            instance = new DisabledRulesService();
        }

        return instance;
    }

    private DisabledRulesService() {}

    public boolean isRuleDisabled(Rule rule, PsiFile file, @Nullable Integer lineNumber) {
        if (lineNumber == null) {
            lineNumber = 1;
        }

        return getDisabledRulesLookupTable(file).isRuleDisabledForLine(rule, lineNumber);
    }

    private DisabledRulesLookupTable getDisabledRulesLookupTable(PsiFile file) {
        var virtualFile = file.getVirtualFile();
        var cacheKey = (virtualFile != null && virtualFile.getCanonicalPath() != null)
            ? virtualFile.getCanonicalPath()
            : file.getName();

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
