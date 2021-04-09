package org.codenarc.idea.testing

import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor
import groovy.transform.CompileStatic
import org.jetbrains.annotations.NotNull

@CompileStatic
class LibraryLightProjectDescriptor extends DefaultLightProjectDescriptor {

    private final TestLibrary myLibrary

    LibraryLightProjectDescriptor(TestLibrary library) {
        myLibrary = library
    }

    @Override
    void configureModule(@NotNull Module module, @NotNull ModifiableRootModel model, @NotNull ContentEntry contentEntry) {
        super.configureModule(module, model, contentEntry)
        myLibrary.addTo(module, model)
    }
}
