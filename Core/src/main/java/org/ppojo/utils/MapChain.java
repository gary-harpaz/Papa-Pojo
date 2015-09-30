package org.ppojo.utils;

import javax.annotation.*;
import java.util.HashMap;
import java.util.Map;

import static org.ppojo.utils.Helpers.*;

/**
 * Created by GARY on 9/23/2015.
 */
public class MapChain {

    private final Map<String,Object> _properties;
    private final MapChain _parentMap;
    private final String _name;
    public MapChain(String name, @Nullable Map<String,Object> localOptions, @Nullable MapChain parent) {


        _name=name;
        _parentMap = parent;
        if (localOptions==null)
            localOptions=new HashMap<>();
        _properties = localOptions;
    }

    public MapChainValue get(String key) {
        return get(key,this);
    }
    public String getString(String key) {
        return as(String.class, get(key).getValue());
    }

    private MapChainValue get(String key,MapChain querySource) {
        Object value = _properties.get(key);
        if (value != null)
            return new MapChainValue(value,querySource,this);
        if (_parentMap != null)
            return _parentMap.get(key,querySource);
        return null;
    }
    public void put(String key,Object value) {
        _properties.put(key,value);
    }

    public void remove(String key) {
        _properties.remove(key);
    }


    public String getName() {
        return _name;
    }

    public Map<String,Object> cloneLocalProperties() {
        return new HashMap<>(_properties);
    }

    public boolean hasLocalProperties() {
        return _properties!=null && !_properties.isEmpty();
    }
}
