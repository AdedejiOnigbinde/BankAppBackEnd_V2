package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class AccountLocked extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AccountLocked(String message) {
        super(message);
    }

}
