package org.ppojo.exceptions;

/**
 * Created by GARY on 9/30/2015.
 */
public class UnsupportedJsonElementType extends RuntimeException{
    public UnsupportedJsonElementType(Class fieldType,String fieldName,Class declaringClass) {
        super("Unable to match "+declaringClass.toString()+" field "+fieldName+" of type "
                +fieldType+" to Json Element type. Please update the type map");
    }

}
