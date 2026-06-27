package com.base.BaseDependencies.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountCreation;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountLocked;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BeneficiaryExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BeneficiaryNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BillNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientAlreadyExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.DepositRequestNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InsufficentFunds;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidPassword;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidToken;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.LoanNotFound;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ AccountNotFound.class, ClientNotFound.class, BeneficiaryNotFound.class,
            BillNotFound.class, LoanNotFound.class, DepositRequestNotFound.class })
    public ResponseEntity<String> handleNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler({ ClientAlreadyExists.class, InvalidToken.class, BadCredentialsException.class,
            BeneficiaryExists.class, AccountCreation.class, InvalidTransaction.class,
            InvalidPassword.class })
    public ResponseEntity<String> handleBadRequestException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(InsufficentFunds.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficentFunds exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }

    @ExceptionHandler(AccountLocked.class)
    public ResponseEntity<String> handleAccountLockedException(AccountLocked exception) {
        return ResponseEntity.status(HttpStatus.LOCKED).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }

}
