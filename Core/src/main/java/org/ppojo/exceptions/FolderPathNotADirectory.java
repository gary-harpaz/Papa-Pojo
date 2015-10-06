package org.ppojo.exceptions;

/**
 * Created by GARY on 10/7/2015.
 */
public class FolderPathNotADirectory extends RuntimeException {
    public FolderPathNotADirectory(String message) {
        super(message);
    }
}
