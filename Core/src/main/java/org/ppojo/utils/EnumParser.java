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

package org.ppojo.utils;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Helper class for parsing enum values represented as Strings. The {@link EnumParser#TryParse} method will return
 * the unknown value, which can be set to null, in case the value does not translate to a valid enum member
 * instead of throwing an exception like Enum.valueOf.
 */
public class EnumParser<T> {

    private final HashMap<String,T> _map;
    private final Class<T> _theClass;
    private final T _unknownValue;

    public EnumParser(@Nonnull Class<T> theClass, @Nullable T unknownValue) {
        _map=new HashMap<>();
        _unknownValue=unknownValue;
        for (T t : theClass.getEnumConstants()) {
            _map.put(t.toString(),t);
        }
        _theClass=theClass;
    }

    public @Nonnull T Parse(String str) {
        T result=_map.getOrDefault(str, _unknownValue);
        if (result==null)
            throw new RuntimeException("Could not parse "+str+" to enum "+_theClass);
        return result;
    }

    public @Nullable T TryParse(String str) {
        return _map.getOrDefault(str, _unknownValue);
    }
}
