package com.base.BaseDependencies.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.RequestDtos.DepositRequestDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.DepositRequestNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.DepositRequest;
import com.base.BaseDependencies.Repository.AccountRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Repository.DepositRequestRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepositRequestService {
    private AccountRepo accountRepo;
    private DepositRequestRepo depositRequestRepo;
    private ClientRepo clientRepo;
    private JwtManager tokenManager;
    private ModelMapper modelMapper;

    public String createDepositRequest(DepositRequestDto request, String token) {
        DepositRequest dRequest = new DepositRequest();
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        if (getClient.getAccounts().size() < 2
                && (request.getSplitCheckingAmount() > 0 && request.getSplitSavingsAmount() > 0)) {
            throw new InvalidTransaction(ErrorMessageConstants.DEPOSIT_SPLIT_EXCEPTION_MESSAGE);
        } else if (request.getSplitCheckingAmount() > 0 && request.getSplitSavingsAmount() > 0
                && (request.getSplitCheckingAmount() + request.getSplitSavingsAmount() != request.getCheckAmount())) {
            throw new InvalidTransaction(ErrorMessageConstants.DEPOSIT_SPLIT_TOTAL_EXCEPTION_MESSAGE);
        }
        Account checkingAccount = determineCheckingAccount(getClient.getAccounts());

        dRequest.setCheckNumber(request.getCheckNumber());
        dRequest.setCheckBank(request.getCheckBank());
        dRequest.setCheckAmount(request.getCheckAmount());
        dRequest.setDescription(request.getDescription());
        dRequest.setStatus(GeneralMessageConstants.IN_REVIEW_STATUS);
        dRequest.setSplitCheckingAmount(request.getSplitCheckingAmount());
        dRequest.setSplitSavingsAmount(request.getSplitSavingsAmount());

        if (checkingAccount == null) {
            dRequest.setDepositAccount(getClient.getAccounts().get(0));
        } else {
            dRequest.setDepositAccount(checkingAccount);
        }
        depositRequestRepo.save(dRequest);
        return GeneralMessageConstants.SUCCESSFUL_DEPOSIT_REQUEST_MESSAGE;
    }

    public String deposit(Map<String, String> request, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));

        if (getClient.getRoles().get(0).getRoleName().equals("ADMIN")) {
            DepositRequest deposit = depositRequestRepo.findById(Integer.parseInt(request.get("depositRequestId")))
                    .orElseThrow(() -> new DepositRequestNotFound(
                            ErrorMessageConstants.DEPOSIT_NOT_FOUND_EXCEPTION_MESSAGE));
            if (request.get("status").equals(GeneralMessageConstants.APPROVE_STATUS)) {
                if (deposit.getSplitCheckingAmount() > 0 && deposit.getSplitSavingsAmount() > 0) {
                    List<Account> clientAccountList = deposit.getDepositAccount().getOwnerId().getAccounts();
                    HashMap<String, Account> accountMap = determineAccountType(clientAccountList);
                    Account checkingsAccount = accountMap.get(GeneralMessageConstants.CHECKINGS_ACCOUNT);
                    Account savingsAccount = accountMap.get(GeneralMessageConstants.SAVINGS_ACCOUNT);
                    checkingsAccount.setBalance(checkingsAccount.getBalance() + deposit.getSplitCheckingAmount());
                    savingsAccount.setBalance(savingsAccount.getBalance() + deposit.getSplitSavingsAmount());
                    accountRepo.saveAll(Arrays.asList(checkingsAccount, savingsAccount));
                } else {
                    Account depositAccount = deposit.getDepositAccount();
                    depositAccount.setBalance(depositAccount.getBalance() + deposit.getCheckAmount());
                    accountRepo.save(depositAccount);
                }
                deposit.setStatus(request.get("status"));
            } else {
                deposit.setStatus(request.get("status"));
            }
            depositRequestRepo.save(deposit);
        } else {
            throw new InvalidTransaction(ErrorMessageConstants.UAUTHORIZED_REQUEST_EXCEPTION_MESSAGE);
        }
        return GeneralMessageConstants.SUCCESSFUL_DEPOSIT_MESSAGE;
    }

    public List<DepositRequestDto> getAllClientDepositRequest(String token) {
        String userName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(userName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        List<DepositRequestDto> response = new ArrayList<>();

        getClient.getAccounts().stream().forEach(account -> {
            Optional<List<DepositRequest>> depositrequestList = depositRequestRepo.findByDepositAccount(account);
            if (depositrequestList.isPresent()) {
                depositrequestList.get().stream().forEach(depositRequest -> {
                    DepositRequestDto mappedRequest = modelMapper.map(depositRequest,
                            DepositRequestDto.class);
                    response.add(mappedRequest);
                });
            }
        });

        return response;
    }

    public List<DepositRequestDto> getAllDepositRequest() {
        List<DepositRequestDto> response = new ArrayList<>();
        depositRequestRepo.findAll().stream().forEach(depositRequest -> {
            DepositRequestDto mappDepositRequest = modelMapper.map(depositRequest, DepositRequestDto.class);
            response.add(mappDepositRequest);
        });
        return response;
    }

    private Account determineCheckingAccount(List<Account> senderAccountList) {
        for (Account account : senderAccountList) {
            if (GeneralMessageConstants.CHECKINGS_ACCOUNT.equalsIgnoreCase(account.getAccountType())) {
                return account;
            }
        }
        return null;
    }

    private HashMap<String, Account> determineAccountType(List<Account> senderAccountList) {
        HashMap<String, Account> accountMap = new HashMap<>();
        for (Account account : senderAccountList) {
            if (GeneralMessageConstants.CHECKINGS_ACCOUNT.equalsIgnoreCase(account.getAccountType())) {
                accountMap.put(GeneralMessageConstants.CHECKINGS_ACCOUNT, account);
            } else {
                accountMap.put(GeneralMessageConstants.SAVINGS_ACCOUNT, account);
            }
        }
        return accountMap;
    }
}
