package org.ppojo.exceptions;

import org.ppojo.ArtifactTypes;
import org.ppojo.data.JsonElementTypes;

/**
 * Created by GARY on 9/30/2015.
 */
public class ArtifactElementTypeMismatch extends RuntimeException {
    public ArtifactElementTypeMismatch(String message) {
        super(message);
    }

    public ArtifactElementTypeMismatch(String propertyName, ArtifactTypes artifactType, JsonElementTypes expectedElementType, JsonElementTypes elementType
            , String artifactName, String templateFilePath) {
        super("Type mismatch in field "+propertyName+" artifact type "+artifactType+". Expected "+expectedElementType+", got "+elementType+"."
                +" for artifact "+artifactName+" in "+templateFilePath);
    }
}
