package org.codenarc.idea;

import com.intellij.openapi.components.NamedComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: cedric
 * Date: 20/01/11
 * Time: 23:24
 * <p>
 * This component is responsible for loading the rules from the classpath, and "converting" them into IntelliJ IDEA
 * inspections. This is done by dynamically generating subclasses of {@link CodeNarcInspectionTool} class.
 *
 * @author Cédric Champeau
 */
public class CodeNarcComponent implements NamedComponent {
    public static final String BASE_MESSAGES_BUNDLE = "codenarc-base-messages";

    @NotNull
    @Override
    public String getComponentName() {
        return "CodeNarcComponent";
    }

}
