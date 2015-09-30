package org.ppojo.exceptions;

/**
 * Created by GARY on 9/24/2015.
 */
public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException(String message) {
        super(message);
    }
}
