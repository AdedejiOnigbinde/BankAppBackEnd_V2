package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class InsufficentFunds extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InsufficentFunds(String message) {
        super(message);
    }

}
