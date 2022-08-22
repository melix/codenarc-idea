package org.codenarc.idea.testing;

import com.intellij.jarRepository.JarRepositoryManager;
import com.intellij.jarRepository.RemoteRepositoryDescription;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.DependencyScope;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.libraries.ui.OrderRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.utils.library.RepositoryLibraryProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class RepositoryTestLibrary implements TestLibrary {
    public RepositoryTestLibrary(String... coordinates) {
        this(DependencyScope.COMPILE, coordinates);
    }

    public RepositoryTestLibrary(String coordinates, DependencyScope dependencyScope) {
        this(dependencyScope, coordinates);
    }

    private RepositoryTestLibrary(DependencyScope dependencyScope, String... coordinates) {
        if (coordinates.length == 0) {
            throw new IllegalArgumentException("There must be at least one coordinate");
        };
        myCoordinates = coordinates;
        myDependencyScope = dependencyScope;
    }

    @Override
    public void addTo(@NotNull Module module, @NotNull ModifiableRootModel model) {
        final LibraryTable.ModifiableModel tableModel = model.getModuleLibraryTable().getModifiableModel();
        Library library = tableModel.createLibrary(myCoordinates[0]);
        final Library.ModifiableModel libraryModel = library.getModifiableModel();

        for (String coordinates : myCoordinates) {
            Collection<OrderRoot> roots = loadRoots(module.getProject(), coordinates);
            for (OrderRoot root : roots) {
                libraryModel.addRoot(root.getFile(), root.getType());
            }
        }


        WriteAction.runAndWait(() -> {
                libraryModel.commit();
                tableModel.commit();
        });

        model.findLibraryOrderEntry(library).setScope(myDependencyScope);
    }

    public static Collection<OrderRoot> loadRoots(Project project, String coordinates) {
        RepositoryLibraryProperties libraryProperties = new RepositoryLibraryProperties(coordinates, true);
        Collection<OrderRoot> roots = JarRepositoryManager.loadDependenciesModal(project, libraryProperties, false, false, null, new ArrayList<>(List.of(RemoteRepositoryDescription.MAVEN_CENTRAL)));
        if (roots.isEmpty()) {
            throw new IllegalArgumentException("Roots are empty");
        }
        return roots;
    }

    private final String[] myCoordinates;
    private final DependencyScope myDependencyScope;
}
