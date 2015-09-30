package org.ppojo.exceptions;

import org.ppojo.ArtifactTypes;
import org.ppojo.data.TemplateFileData;

/**
 * Created by GARY on 9/30/2015.
 */
public class UnsupportedTemplateProperty extends RuntimeException {
    public UnsupportedTemplateProperty(String propertyName,String templateFilePath) {
        super("Invalid property "+propertyName+". Unsupported for type "+ TemplateFileData.class.getSimpleName()+" in "+templateFilePath);
    }
}
