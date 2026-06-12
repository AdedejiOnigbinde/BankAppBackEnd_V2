package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class BeneficiaryNotFound extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BeneficiaryNotFound(String message) {
        super(message);
    }
}
