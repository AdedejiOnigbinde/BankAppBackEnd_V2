package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class BeneficiaryNotFound extends RuntimeException {

    public BeneficiaryNotFound(String message) {
        super(message);
    }
}
