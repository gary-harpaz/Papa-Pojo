package org.ppojo.utils;

/**
 * Created by GARY on 9/25/2015.
 */
public class ValidationError {
    private final String _message;
    public ValidationError(String message) {
        _message=message;
    }
    public String getMessage() {
        return _message;
    }
}
