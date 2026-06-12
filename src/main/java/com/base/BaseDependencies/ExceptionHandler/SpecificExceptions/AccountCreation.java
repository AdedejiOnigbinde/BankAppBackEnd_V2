package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class AccountCreation extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public AccountCreation(String message) {
        super(message);
    }


}
