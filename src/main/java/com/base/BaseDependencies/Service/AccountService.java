package com.base.BaseDependencies.Service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Dtos.AccountDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Repository.AccountRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Utils.AccountNumberGenerator;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AccountService {
    private AccountRepo accountRepo;
    private ClientRepo clientRepo;
    private Environment env;
    private JwtManager tokenManager;
    private AccountNumberGenerator accountNumberGenerator;
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto newAccountDto, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Optional<Client> getClient = clientRepo.findByUserName(ownerUserName);
        if (getClient.isPresent()) {
            Account newAccount = modelMapper.map(newAccountDto, Account.class);
            newAccount.setAccountNumber(accountNumberGenerator.generateAccountNumber(12));
            newAccount.setOwnerId(getClient.get());
            return accountRepo.save(newAccount);
        }
        throw new ClientNotFound(env.getProperty("CLIENT_NOT_FOUND_EXCEPTION_MESSAGE"));
    }

    public Boolean deleteAccount(Long accountId, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Optional<Client> getClient = clientRepo.findByUserName(ownerUserName);
        if (getClient.isEmpty()) {
            throw new ClientNotFound(env.getProperty("CLIENT.NOT.FOUND_EXCEPTION.MESSAGE"));
        }
        Optional<Account> getAccount = accountRepo.findById(accountId);
        if (getAccount.isEmpty()) {
            throw new AccountNotFound(env.getProperty("ACCOUNT.NOT.FOUND.EXCEPTION.MESSAGE"));
        }
        accountRepo.deleteByAccountNumberAndOwnerId(accountId, getClient.get().getClientId());
        return true;
    }

    public boolean updateAccountBalance(Account account, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Optional<Client> getClient = clientRepo.findByUserName(ownerUserName);
        if (getClient.isEmpty()) {
            throw new ClientNotFound(env.getProperty("CLIENT.NOT.FOUND_EXCEPTION.MESSAGE"));
        } else if (account.getOwnerId().getClientId() != getClient.get().getClientId()) {
            throw new InvalidTransaction(env.getProperty("TRANSACTION.INVALID.TRANSACTION.EXCEPTION.MESSAGE"));
        }
        accountRepo.save(account);
        return true;

    }

    public List<Account> getAccountByClientUserName(String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Optional<Client> getClient = clientRepo.findByUserName(ownerUserName);
        if (getClient.isEmpty()) {
            throw new ClientNotFound(env.getProperty("CLIENT.NOT.FOUND_EXCEPTION.MESSAGE"));
        }
        Optional<List<Account>> accountList = accountRepo.findByOwnerId(getClient.get());
        if (accountList.isEmpty()) {
            throw new AccountNotFound(env.getProperty("ACCOUNT.NOT.FOUND.EXCEPTION.MESSAGE"));
        }
        return accountList.get();
    }

    public Account getAccountById(long accountNumber, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Optional<Client> getClient = clientRepo.findByUserName(ownerUserName);
        if (getClient.isEmpty()) {
            throw new ClientNotFound(env.getProperty("CLIENT.NOT.FOUND_EXCEPTION.MESSAGE"));
        }
        Optional<Account> getAccount = accountRepo.findByAccountNumberAndOwnerId(accountNumber, getClient.get());
        if (getAccount.isEmpty()) {
            throw new AccountNotFound(env.getProperty("ACCOUNT.NOT.FOUND.EXCEPTION.MESSAGE"));
        }
        return getAccount.get();
    }

    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }
}
