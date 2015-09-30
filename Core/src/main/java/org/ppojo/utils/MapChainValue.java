package org.ppojo.utils;


import org.ppojo.utils.MapChain;

import javax.annotation.*;

/**
 * Created by GARY on 9/23/2015.
 */
public class MapChainValue {
    private final Object _value;
    private final MapChain _querySource;
    private final MapChain _valueSource;

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
