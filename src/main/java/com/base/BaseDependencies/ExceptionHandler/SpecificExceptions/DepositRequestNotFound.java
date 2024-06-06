package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class DepositRequestNotFound extends RuntimeException {
    public DepositRequestNotFound(String message) {
        super(message);
    }

}
