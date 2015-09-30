package org.ppojo.exceptions;

import org.ppojo.data.JsonElementTypes;

/**
 * Created by GARY on 9/30/2015.
 */
public class TemplateElementTypeMismatch extends RuntimeException {

    public TemplateElementTypeMismatch(String propertyName, JsonElementTypes expectedElementType, JsonElementTypes elementType, String templateFilePath) {
        super("Type mismatch in field "+propertyName+". Expected "+expectedElementType+" got "+elementType+". In "+templateFilePath);

    }

}
