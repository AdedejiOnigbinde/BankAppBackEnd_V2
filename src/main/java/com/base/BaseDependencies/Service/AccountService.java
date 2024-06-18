package com.base.BaseDependencies.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Dtos.AccountDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountCreation;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientAlreadyExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Repository.AccountRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Utils.AccountNumberGenerator;
import com.base.BaseDependencies.Utils.JwtManager;
import com.base.BaseDependencies.Constants.ErrorMessageConstants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountService {
    private AccountRepo accountRepo;
    private ClientRepo clientRepo;
    private JwtManager tokenManager;
    private AccountNumberGenerator accountNumberGenerator;
    private ModelMapper modelMapper;

    public AccountDto createAccount(Map<String, String> accountType, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        List<Account> clientAccounts = getClient.getAccounts();
        clientAccounts.forEach(account -> {
            if (account.getAccountType().equalsIgnoreCase(accountType.get("accountType"))) {
                throw new ClientAlreadyExists(ErrorMessageConstants.ACCOUNT_CREATION_EXCEPTION_MESSAGE);
            }else if(!accountType.get("accountType").equalsIgnoreCase("savings") && !accountType.get("accountType").equalsIgnoreCase("checkings") ){
                throw new AccountCreation(ErrorMessageConstants.ACCOUNT_BAD_CREATION_EXCEPTION_MESSAGE);
            }
        });
        Account newAccount = Account.builder()
                .ownerId(getClient)
                .accountNumber(accountNumberGenerator.generateAccountNumber(12))
                .accountType(accountType.get("accountType"))
                .accountStatus("ACTIVE")
                .dailyTransferLimit(100000)
                .build();
        Account savedAccount = accountRepo.save(newAccount);

        return mapAccountToDto(savedAccount);
    }

    public Boolean deleteAccount(Long accountId, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));

        accountRepo.deleteByAccountNumberAndOwnerId(accountId, getClient);
        return true;
    }

    public List<AccountDto> getAccountByClientUserName(String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        List<Account> accountList = accountRepo.findByOwnerId(getClient)
                .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));
        return accountList.stream().map(this::mapAccountToDto).collect(Collectors.toList());

    }

    public AccountDto getAccountById(long accountNumber, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Account getAccount = accountRepo.findByAccountNumberAndOwnerId(accountNumber, getClient)
                .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));
        return mapAccountToDto(getAccount);
    }

    public Page<Account> getAllAccounts(int page) {

        PageRequest pageRequest = PageRequest.of(page, page);
        return accountRepo.findAll(pageRequest);
    }

    private AccountDto mapAccountToDto(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }
}
