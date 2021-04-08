package org.codenarc.idea.testing

import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModifiableRootModel
import groovy.transform.CompileStatic
import org.jetbrains.annotations.NotNull

@CompileStatic
final class CompoundTestLibrary implements TestLibrary {

    private final TestLibrary[] myLibraries

    CompoundTestLibrary(TestLibrary... libraries) {
        assert libraries.length > 0
        myLibraries = libraries
    }

    @Override
    void addTo(@NotNull Module module, @NotNull ModifiableRootModel model) {
        for (library in myLibraries) {
            library.addTo(module, model)
        }
    }
}
