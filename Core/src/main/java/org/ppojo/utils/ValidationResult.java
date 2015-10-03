package org.ppojo.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GARY on 9/25/2015.
 */
public class ValidationResult {
    private final List<ValidationWarning> _warnings;
    private final List<ValidationError> _errors;
    private boolean _throwsExceptionOnFirstErr;
    public ValidationResult(boolean throwsExceptionOnFirstErr) {
        _warnings=new ArrayList<>();
        _errors=new ArrayList<>();
        _throwsExceptionOnFirstErr=throwsExceptionOnFirstErr;

    }
    public List<ValidationWarning> getWarnings() {
        return _warnings;
    }

    public List<ValidationError> getErrors() {
        return _errors;
    }

    public void addError(String message) {
        _errors.add(new ValidationError(message));
        if (_throwsExceptionOnFirstErr)
            throw new RuntimeException(message);

    }

    public void addWarning(String message) {
        _warnings.add(new ValidationWarning(message));

    }

    public boolean hasErrors() {
        return !_errors.isEmpty();
    }
}
