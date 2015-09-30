package org.ppojo.exceptions;

import org.ppojo.ArtifactTypes;

/**
 * Created by GARY on 9/30/2015.
 */
public class UnsupportedArtifactProperty extends RuntimeException {
    public UnsupportedArtifactProperty(String message) {
        super(message);
    }

    public UnsupportedArtifactProperty(String propertyName,ArtifactTypes artifactType,String artifactName,String templateFilePath) {
        super("Invalid property "+propertyName+". Unsupported for artifact type "+artifactType+" named "+artifactName+" in "+templateFilePath);
    }
}
