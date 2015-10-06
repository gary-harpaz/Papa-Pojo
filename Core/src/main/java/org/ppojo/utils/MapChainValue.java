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


import javax.annotation.*;

/**
 * Created by GARY on 9/23/2015.
 */
public class MapChainValue {
    private final Object _value;
    private final MapChain _querySource;
    private final MapChain _valueSource;
    public MapChain getValueSource() {
        return _valueSource;
    }

    public MapChainValue(Object value,@Nonnull MapChain querySource,@Nonnull MapChain valueSource){
        if (querySource==null)
            throw new NullPointerException("querySource");
        if (valueSource==null)
            throw new NullPointerException("valueSource");

        _value=value;
        _querySource=querySource;
        _valueSource=valueSource;
    }

    public boolean isLocalValue() {
        return _querySource==_valueSource;
    }

    public Object getValue() {
        return _value;
    }

    public String getValueSourceName() {
        return _valueSource.getName();
    }
}
