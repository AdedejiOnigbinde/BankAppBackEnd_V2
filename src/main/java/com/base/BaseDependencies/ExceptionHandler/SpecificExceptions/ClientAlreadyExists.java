package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class ClientAlreadyExists extends RuntimeException {

    public ClientAlreadyExists(String message) {
        super(message);
    }

}
