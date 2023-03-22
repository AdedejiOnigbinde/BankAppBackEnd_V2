package com.base.BaseDependencies.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientAlreadyExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InsufficentFunds;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFound.class)
    public ResponseEntity<?> handleAccountNotFoundException(AccountNotFound exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ClientAlreadyExists.class)
    public ResponseEntity<?> handleClientAlreadyExistsException(ClientAlreadyExists exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ClientNotFound.class)
    public ResponseEntity<?> handleClientNotFoundException(ClientNotFound exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(InsufficentFunds.class)
    public ResponseEntity<?> handleInsufficentFundsException(InsufficentFunds exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidTransaction.class)
    public ResponseEntity<?> handleInvalidTransactionException(InvalidTransaction exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

}
