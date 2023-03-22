package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class InvalidTransaction extends RuntimeException {

    public InvalidTransaction(String message) {
        super(message);
    }

}
