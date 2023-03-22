package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class InsufficentFunds extends RuntimeException {

    public InsufficentFunds(String message) {
        super(message);
    }

}
