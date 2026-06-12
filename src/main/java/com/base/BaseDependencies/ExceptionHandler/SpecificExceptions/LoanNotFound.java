package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class LoanNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public LoanNotFound(String message) {
        super(message);
    }
}
