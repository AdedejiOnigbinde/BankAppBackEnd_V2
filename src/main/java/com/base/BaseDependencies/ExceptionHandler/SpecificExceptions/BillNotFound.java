package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class BillNotFound extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public BillNotFound (String message) {
        super(message);
    }
}
