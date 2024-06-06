package com.base.BaseDependencies.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountCreation;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BeneficiaryExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BeneficiaryNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BillNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientAlreadyExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.DepositRequestNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InsufficentFunds;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidToken;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.LoanNotFound;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ AccountNotFound.class, ClientNotFound.class, InsufficentFunds.class,
            InvalidTransaction.class, BeneficiaryNotFound.class, BillNotFound.class, LoanNotFound.class,
            DepositRequestNotFound.class })
    public ResponseEntity<String> handleNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler({ ClientAlreadyExists.class, InvalidToken.class, BadCredentialsException.class,
            BeneficiaryExists.class, AccountCreation.class })
    public ResponseEntity<String> handleBadRequestException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

}
