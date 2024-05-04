package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class BillNotFound extends RuntimeException{
    public BillNotFound (String message) {
        super(message);
    }
}
