package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class LoanNotFound extends RuntimeException {
    public LoanNotFound(String message) {
        super(message);
    }
}
