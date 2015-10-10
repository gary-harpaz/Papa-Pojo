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

/**
 * Option which affects the various capitalization styles applied to artifact members. By selecting different {@link CapitalizationTypes}
 * you can choose the style in accordance with your preferred naming convention.
 */
public enum CapitalizationTypes {
    None,
    camelCase,
    PascalCase,
    ALL_CAPS
    ;

    private static EnumParser<CapitalizationTypes> _parser=new EnumParser<>(CapitalizationTypes.class,null);

    public static CapitalizationTypes Parse(String capitalizationType) {
        return _parser.Parse(capitalizationType);
    }
}
