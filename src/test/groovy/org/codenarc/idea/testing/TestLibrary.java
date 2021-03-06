package org.codenarc.idea.testing;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import org.jetbrains.annotations.NotNull;

public interface TestLibrary {

  default void addTo(@NotNull Module module) {
    ModuleRootModificationUtil.updateModel(module, model -> addTo(module, model));
  }

  void addTo(@NotNull Module module, @NotNull ModifiableRootModel model);

  @NotNull
  default TestLibrary plus(@NotNull TestLibrary library) {
    return new CompoundTestLibrary(this, library);
  }
}
