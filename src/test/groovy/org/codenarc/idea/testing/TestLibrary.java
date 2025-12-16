package org.codenarc.idea.testing;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TestLibrary {
  default void addTo(Module module) {
    ModuleRootModificationUtil.updateModel(module, model -> addTo(module, model));
  }

  void addTo(Module module, ModifiableRootModel model);

  default TestLibrary plus(TestLibrary library) {
    return new CompoundTestLibrary(this, library);
  }
}
