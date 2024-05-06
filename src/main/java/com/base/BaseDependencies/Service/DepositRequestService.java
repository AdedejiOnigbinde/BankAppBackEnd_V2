package com.base.BaseDependencies.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.RequestDtos.DepositRequestDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Repository.AccountRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepositRequestService {
    private AccountRepo accountRepo;
    private ClientRepo clientRepo;
    private JwtManager tokenManager;

    public String createDepositRequest(DepositRequestDto request, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        if (request.getSpitCheckingAmount() > 0 && request.getSpitSavingsAmount() > 0
                && (request.getSpitCheckingAmount() + request.getSpitSavingsAmount() == request.getCheckAmount())) {
                    
        }

        return GeneralMessageConstants.SUCCESSFUL_DEPOSIT_REQUEST_MESSAGE;
    }

    private Account determineCheckingAccount(List<Account> senderAccountList) {
        for (Account account : senderAccountList) {
            if (GeneralMessageConstants.CHECKINGS_ACCOUNT.equalsIgnoreCase(account.getAccountType())) {
                return account;
            }
        }
        return null;
    }
}
