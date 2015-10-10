/*
 * Copyright (c) 2015.  Gary Harpaz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ppojo.data;

import org.ppojo.ArtifactBase;
import org.ppojo.*;
import org.ppojo.ArtifactTypes;
import java.util.Map;


/**
 * Base class for representing a deserialized artifact defined in JSON format within a papa pojo template file.
 * This information is very raw and is parsed and validated before being transformed to one of the classes
 * derived from {@link ArtifactBase} and subsequently to generate the artifact source code.
 * @see ArtifactParser
 * @see ArtifactSerializer
 * @see ArtifactBase
 */
public abstract class ArtifactData {
    /**
     * String representation of {@link ArtifactTypes}
     */
    public String type;
    /**
     * Name of the artifact. For example class name, interface name etc.
     */
    public String name;
    /**
     * Optional property used if the artifact should not be created in the same folder as the template.
     * Output path can be relative to the template folder.
     */
    public String path;
    /**
     * Not implemented
     */
    public String nestInArtifact;
    /**
     * When set to true the options and the option source values used to configure the artifact are output to the standard output.
     */
    public boolean debugOptions;
    /**
     * Options to configure the artifact that are specified at artifact level, specific only to current artifact.
     * When specified at this level they will override options defined at the template level and default options levels.
     * Note that this field is transient. The values are deserialized manually in {@link ArtifactSerializer#deserialize}.*
     */
    public transient Map<String,Object> options;



}
