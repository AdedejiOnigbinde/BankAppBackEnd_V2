package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class ClientAlreadyExists extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ClientAlreadyExists(String message) {
        super(message);
    }

}
