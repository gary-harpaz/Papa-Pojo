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

import org.ppojo.*;

/**
 * Represents a deserialized {@link ArtifactTypes#ImmutableClass} artifact.
 * @see ImmutableClassArtifact
 * @see ArtifactTypes#ImmutableClass
 */
public class ImmutableClassData extends ClassArtifactData {
    /**
     * The class name used for the data field, or data behind the immutable instance.
     * This should correspond to an {@link ArtifactTypes#Pojo} artifact name.
     */
    public String dataArtifact;
}
