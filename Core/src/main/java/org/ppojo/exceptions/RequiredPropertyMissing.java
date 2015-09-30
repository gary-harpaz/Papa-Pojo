package org.ppojo.exceptions;

/**
 * Created by GARY on 9/29/2015.
 */
public class RequiredPropertyMissing extends RuntimeException {
    public RequiredPropertyMissing(String message) {
        super(message);
    }
}
