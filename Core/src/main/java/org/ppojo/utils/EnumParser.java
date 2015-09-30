package org.ppojo.utils;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Created by GARY on 9/28/2015.
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
