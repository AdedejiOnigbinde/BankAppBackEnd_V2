package com.base.BaseDependencies.Service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Dtos.AccountDto;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Repository.AccountRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Utils.AccountNumberGenerator;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

import java.util.Collections;

@AllArgsConstructor
@Service
public class AccountService {
    private  AccountRepo accountRepo;
    private  ClientRepo clientRepo;
    private  JwtManager tokenManager;
    private  AccountNumberGenerator accountNumberGenerator;
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto newAccountDto, String token) {
        Account newAccount = modelMapper.map(newAccountDto, Account.class);
        Integer ownerId = tokenManager.parseToken(token);
        Client getClient = clientRepo.findById(ownerId).orElse(null);
        if (getClient == null) {
            return null;
        } else {
            newAccount.setAccountNumber(accountNumberGenerator.generateAccountNumber(12));
            newAccount.setOwnerId(getClient);
            return accountRepo.save(newAccount);
        }
    }

    public Boolean deleteAccount(Long accountId, String token) {
        Integer ownerId = tokenManager.parseToken(token);
        Client getClient = clientRepo.findById(ownerId).orElse(null);
        Account getAccount = accountRepo.findById(accountId).orElse(null);
        if (getClient == null || getAccount == null) {
            return false;
        } else {
            accountRepo.deleteByAccountNumberAndOwnerId(accountId, ownerId);
            return true;
        }

    }

    public boolean updateAccountBalance(Account account, String token) {
        Integer ownerId = tokenManager.parseToken(token);
        Client getClient = clientRepo.findById(ownerId).orElse(null);
        if (getClient == null) {
            return false;
        }else{
            accountRepo.save(account);
            return true;
        }
    }

    public List<Account> getAccountByClientId(String token) {
        Integer ownerId = tokenManager.parseToken(token);
        Client getClient = clientRepo.findById(ownerId).orElse(null);
        if (getClient == null) {
            return Collections.emptyList();
        } else {
            return accountRepo.findByOwnerId(ownerId);
        }
    }

    public Account getAccountById(long accountNumber, String token) {
        Integer ownerId = tokenManager.parseToken(token);
        Client getClient = clientRepo.findById(ownerId).orElse(null);
        Account getAccount = accountRepo.findById(accountNumber).orElse(null);
        if (getClient == null || getAccount == null) {
            return null;
        } else {
            return getAccount;
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }
}
