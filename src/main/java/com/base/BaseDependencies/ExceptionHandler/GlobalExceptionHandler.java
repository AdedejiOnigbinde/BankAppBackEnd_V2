package com.base.BaseDependencies.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BeneficiaryExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BeneficiaryNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BillNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientAlreadyExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InsufficentFunds;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidToken;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.LoanNotFound;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFound.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFound exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ClientAlreadyExists.class)
    public ResponseEntity<String> handleClientAlreadyExistsException(ClientAlreadyExists exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ClientNotFound.class)
    public ResponseEntity<String> handleClientNotFoundException(ClientNotFound exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(InsufficentFunds.class)
    public ResponseEntity<String> handleInsufficentFundsException(InsufficentFunds exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidTransaction.class)
    public ResponseEntity<String> handleInvalidTransactionException(InvalidTransaction exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidToken.class)
    public ResponseEntity<String> handleInvalidTokenException(InvalidToken exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessageConstants.INVALID_LOGIN_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(BeneficiaryExists.class)
    public ResponseEntity<String> handleBeneficiaryExistsException(BeneficiaryExists exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(BeneficiaryNotFound.class)
    public ResponseEntity<String> handleBeneficiaryNotFoundException(BeneficiaryNotFound exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(BillNotFound.class)
    public ResponseEntity<String> handleBillNotFoundException(BillNotFound exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(LoanNotFound.class)
    public ResponseEntity<String> handleLoanNotFoundException(LoanNotFound exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

}
