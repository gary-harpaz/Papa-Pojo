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

import java.lang.reflect.Array;
import java.util.HashMap;

/**
 * Created by GARY on 10/3/2015.
 */
public class EmptyArray {

    private static HashMap<Class,Object> _emptyArrays=new HashMap<>();
    public static <T> T[] get(Class<T> theClass) {
        Object result=_emptyArrays.get(theClass);
        if (result==null) {
            result=Array.newInstance(theClass,0);
            _emptyArrays.put(theClass,result);
        }
        return (T[])result;
    }
}
