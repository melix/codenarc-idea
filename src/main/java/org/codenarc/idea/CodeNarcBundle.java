package org.codenarc.idea;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

@NullMarked
public class CodeNarcBundle extends DynamicBundle {
    @NonNls
    private static final String BUNDLE = "messages.CodeNarcBundle";
    private static final CodeNarcBundle INSTANCE = new CodeNarcBundle();

    private CodeNarcBundle() {
        super(BUNDLE);
    }

    public static @Nls String message(@PropertyKey(resourceBundle = BUNDLE) String key, @Nullable Object... params) {
        return INSTANCE.getMessage(key, params);
    }

    public static Supplier<String> messagePointer(
        @PropertyKey(resourceBundle = BUNDLE) String key,
        @Nullable Object... params
    ) {
        return INSTANCE.getLazyMessage(key, params);
    }
}
