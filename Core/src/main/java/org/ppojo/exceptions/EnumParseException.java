package org.ppojo.exceptions;

/**
 * Created by GARY on 9/30/2015.
 */
public class EnumParseException extends RuntimeException {
    public EnumParseException(String value,String propertyName,String artifactName,String templateFilePath) {
        super("Invalid value "+value+" for option "+propertyName+" at artifact "+artifactName+" in "+templateFilePath);
    }

    public EnumParseException(String value, String propertyName, String templateFilePath) {
        super("Invalid value "+value+" for option "+propertyName+" in "+templateFilePath);
    }
}
