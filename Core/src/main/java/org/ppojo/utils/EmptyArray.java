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
