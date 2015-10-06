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


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by GARY on 9/23/2015.
 */
public class MapChain {

    private final String _name;
    private boolean _hasLocalProperties;
    private ImmutableMap<String,MapChainValue> _values=ImmutableHashMap.empty();
    public MapChain(String name, @Nullable Map<String,Object> localOptions, @Nullable MapChain parent) {
        if ((parent!=null && parent._values.size()>0) || (localOptions!=null && localOptions.size()>0)) {
            HashMap<String,MapChainValue> values=new HashMap<>();
            if (parent!=null)
                for (Map.Entry<String, MapChainValue> entry : parent._values.entries()) {
                    values.put(entry.getKey(),new MapChainValue(entry.getValue().getValue(),this,entry.getValue().getValueSource()));
                }
            if (localOptions!=null)
                for (Map.Entry<String, Object> entry : localOptions.entrySet()) {
                    values.put(entry.getKey(),new MapChainValue(entry.getValue(),this,this));
                    _hasLocalProperties= true;
                }
            _values=new ImmutableHashMap<>(values);
        }
        _name=name;
    }

    public MapChainValue get(String key) {
        return _values.get(key);
    }
    public String getName() {
        return _name;
    }

    public Map<String,Object> cloneLocalProperties() {
        HashMap<String,Object> result=new HashMap<>();
        for (Map.Entry<String, MapChainValue> entry : _values.entries()) {
            if (entry.getValue().isLocalValue())
                result.put(entry.getKey(),entry.getValue().getValue());
        }
        return result;
    }

    public boolean hasLocalProperties() {
        return _hasLocalProperties;
    }
}
