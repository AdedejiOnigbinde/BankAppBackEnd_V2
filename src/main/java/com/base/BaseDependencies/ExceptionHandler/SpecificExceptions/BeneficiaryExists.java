package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class BeneficiaryExists extends RuntimeException {
    public BeneficiaryExists(String message) {
        super(message);
    }
}
