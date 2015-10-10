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

package org.ppojo;

import org.ppojo.utils.EnumParser;

import javax.annotation.Nonnull;

/**
 * Enumerates the various artifact types supported by the papa pojo engine. See the wiki for more details and examples of artifact types.
 */
public enum ArtifactTypes {
    Unknown,
    Pojo,
    Interface,
    ImmutableClass,
    FieldEnum,
    FluentBuilder;

    private static final EnumParser<ArtifactTypes> _parser =new EnumParser<>(org.ppojo.ArtifactTypes.class,ArtifactTypes.Unknown);

    public static @Nonnull ArtifactTypes Parse(String artifactType) {
        return _parser.Parse(artifactType);
    }
}
