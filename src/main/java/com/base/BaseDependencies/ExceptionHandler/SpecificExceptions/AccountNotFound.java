package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class AccountNotFound extends RuntimeException {

    public AccountNotFound(String message) {
        super(message);
    }

}
