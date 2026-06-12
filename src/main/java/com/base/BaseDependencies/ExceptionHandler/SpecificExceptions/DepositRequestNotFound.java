package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class DepositRequestNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public DepositRequestNotFound(String message) {
        super(message);
    }

}
