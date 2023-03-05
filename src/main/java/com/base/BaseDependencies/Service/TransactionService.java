package com.base.BaseDependencies.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Dtos.TransactionDto;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Transaction;
import com.base.BaseDependencies.Repository.TransactionRepo;

import lombok.AllArgsConstructor;

import java.util.Collections;

@AllArgsConstructor
@Service
public class TransactionService {
    private TransactionRepo transactionRepo;
    private AccountService accountService;

    public Boolean withdrawal(TransactionDto transDto, String token) {
        Account updatedAccount = accountService.getAccountById(transDto.getFromAcct(), token);
        if (updatedAccount != null) {
            double accountBal = updatedAccount.getBalance();
            double withdrawlAmt = transDto.getTransAmount();
            if (accountBal > withdrawlAmt) {
                updatedAccount.setBalance(accountBal - withdrawlAmt);
                boolean hasUpdated = accountService.updateAccountBalance(updatedAccount, token);
                if (hasUpdated) {
                    Transaction newTransaction = new Transaction();
                    newTransaction.setTransAmount(transDto.getTransAmount());
                    newTransaction.setTransType(transDto.getTransType());
                    newTransaction.setFromAccount(updatedAccount);
                    transactionRepo.save(newTransaction);
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean deposit(TransactionDto transDto, String token) {
        Account updatedAccount = accountService.getAccountById(transDto.getFromAcct(), token);
        if (updatedAccount != null) {
            double accountBal = updatedAccount.getBalance();
            double withdrawlAmt = transDto.getTransAmount();
            updatedAccount.setBalance(accountBal + withdrawlAmt);
            boolean hasUpdated = accountService.updateAccountBalance(updatedAccount, token);
            if (hasUpdated) {
                Transaction newTransaction = new Transaction();
                newTransaction.setTransAmount(transDto.getTransAmount());
                newTransaction.setTransType(transDto.getTransType());
                newTransaction.setFromAccount(updatedAccount);
                transactionRepo.save(newTransaction);
                return true;
            }
        }
        return false;
    }

    public Boolean transfer(TransactionDto transDto, String token) {
        Account fromAccount = accountService.getAccountById(transDto.getFromAcct(), token);
        Account toAccount = accountService.getAccountById(transDto.getToAcct(), token);
        if (fromAccount != null && toAccount != null) {
            double fromAccountBal = fromAccount.getBalance();
            double transAmt = transDto.getTransAmount();
            double toAccountBal = toAccount.getBalance();
            if (fromAccountBal > transAmt && (fromAccountBal > 0 && transAmt > 0)) {
                toAccount.setBalance(fromAccountBal + transAmt);
                fromAccount.setBalance(toAccountBal - transAmt);
                boolean hasToAccountUpdated = accountService.updateAccountBalance(toAccount, token);
                boolean hasFromAccountUpdated = accountService.updateAccountBalance(fromAccount, token);
                if (hasToAccountUpdated && hasFromAccountUpdated) {
                    Transaction newTransaction = new Transaction();
                    newTransaction.setTransAmount(transDto.getTransAmount());
                    newTransaction.setTransType(transDto.getTransType());
                    newTransaction.setFromAccount(fromAccount);
                    newTransaction.setToAcct(transDto.getToAcct());
                    transactionRepo.save(newTransaction);
                    return true;
                }
            }
        }
        return false;
    }

    public List<Transaction> getTransactionByAccountNumber(long accountNumber, String token) {
        Account existingAccount = accountService.getAccountById(accountNumber, token);
        if (existingAccount == null) {
            return Collections.emptyList();
        } else {
            return transactionRepo.findByFromAccount(existingAccount);
        }
    }
}
