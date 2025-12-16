package org.codenarc.idea.testing;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class LibraryLightProjectDescriptor extends DefaultLightProjectDescriptor {
    private final TestLibrary myLibrary;

    public LibraryLightProjectDescriptor(TestLibrary library) {
        myLibrary = library;
    }

    @Override
    public void configureModule( Module module, ModifiableRootModel model, ContentEntry contentEntry) {
        super.configureModule(module, model, contentEntry);
        myLibrary.addTo(module, model);
    }
}
