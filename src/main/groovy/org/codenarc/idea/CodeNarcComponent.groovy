/*
 *
 *
 *   Copyright 2011 Cédric Champeau
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  /
 * /
 */

package org.codenarc.idea

import com.intellij.openapi.components.NamedComponent
import org.jetbrains.annotations.NotNull

/**
 * Created by IntelliJ IDEA.
 * User: cedric
 * Date: 20/01/11
 * Time: 23:24
 *
 * This component is responsible for loading the rules from the classpath, and "converting" them into IntelliJ IDEA
 * inspections. This is done by dynamically generating subclasses of {@link CodeNarcInspectionTool} class.
 *
 * @author Cédric Champeau
 */
class CodeNarcComponent implements NamedComponent {

    public static final String BASE_MESSAGES_BUNDLE = "codenarc-base-messages"

    CodeNarcComponent() {
        // generate classes
        new CodeNarcInspectionToolProvider()
    }

    @NotNull
    String getComponentName() {
        return "CodeNarcComponent"
    }

}
