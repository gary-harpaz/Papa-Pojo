package org.ppojo.exceptions;

import org.ppojo.data.TemplateFileData;

/**
 * Created by GARY on 9/30/2015.
 */
public class UnsupportedTemplateOption extends RuntimeException {
    public UnsupportedTemplateOption(String propertyName,String templateFilePath) {
        super("Invalid option "+propertyName+" in "+templateFilePath);
    }
}
