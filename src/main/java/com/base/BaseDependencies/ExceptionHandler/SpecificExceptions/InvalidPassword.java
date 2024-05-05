package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class InvalidPassword extends RuntimeException {
    public InvalidPassword(String message) {
        super(message);
    }
}
