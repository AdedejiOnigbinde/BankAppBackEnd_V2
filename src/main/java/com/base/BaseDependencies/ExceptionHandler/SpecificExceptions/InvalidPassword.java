package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class InvalidPassword extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidPassword(String message) {
        super(message);
    }
}
