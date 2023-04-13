package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class InvalidToken extends RuntimeException{

    public InvalidToken(String message) {
        super(message);
    }

}
