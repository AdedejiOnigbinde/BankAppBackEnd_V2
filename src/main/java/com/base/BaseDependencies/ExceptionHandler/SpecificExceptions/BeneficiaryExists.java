package com.base.BaseDependencies.ExceptionHandler.SpecificExceptions;

public class BeneficiaryExists extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public BeneficiaryExists(String message) {
        super(message);
    }
}
