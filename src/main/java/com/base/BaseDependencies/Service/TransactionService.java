package com.base.BaseDependencies.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Dtos.TransactionDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InsufficentFunds;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Transaction;
import com.base.BaseDependencies.Repository.TransactionRepo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TransactionService {
    private TransactionRepo transactionRepo;
    private AccountService accountService;
    private Environment env;

    public String withdrawal(TransactionDto transDto, String token) {
        Account updatedAccount = accountService.getAccountById(transDto.getFromAcct(), token);
        double accountBal = updatedAccount.getBalance();
        double withdrawlAmt = transDto.getTransAmount();
        if (accountBal > withdrawlAmt) {
            updatedAccount.setBalance(accountBal - withdrawlAmt);
            accountService.updateAccountBalance(updatedAccount, token);
            Transaction newTransaction = new Transaction();
            newTransaction.setTransAmount(transDto.getTransAmount());
            newTransaction.setTransType(transDto.getTransType());
            newTransaction.setFromAccount(updatedAccount);
            transactionRepo.save(newTransaction);
            return "Withdrawal Successful";
        }
        throw new InsufficentFunds(env.getProperty("TRANSACTION.INSUFFICIENT.FUNDS.EXCEPTION.MESSAGE"));
    }

    public String deposit(TransactionDto transDto, String token) {
        Account updatedAccount = accountService.getAccountById(transDto.getFromAcct(), token);
        double accountBal = updatedAccount.getBalance();
        double withdrawlAmt = transDto.getTransAmount();
        updatedAccount.setBalance(accountBal + withdrawlAmt);
        accountService.updateAccountBalance(updatedAccount, token);
        Transaction newTransaction = new Transaction();
        newTransaction.setTransAmount(transDto.getTransAmount());
        newTransaction.setTransType(transDto.getTransType());
        newTransaction.setFromAccount(updatedAccount);
        transactionRepo.save(newTransaction);
        return "Deposit Successful";
    }

    public String transfer(TransactionDto transDto, String token) {
        Account fromAccount = accountService.getAccountById(transDto.getFromAcct(), token);
        Account toAccount = accountService.getAccountById(transDto.getToAcct(), token);
        double fromAccountBal = fromAccount.getBalance();
        double transAmt = transDto.getTransAmount();
        double toAccountBal = toAccount.getBalance();
        if (fromAccountBal < transAmt) {
            throw new InsufficentFunds(env.getProperty("TRANSACTION.INSUFFICIENT.FUNDS.EXCEPTION.MESSAGE"));
        } else if (fromAccountBal < 0 && transAmt < 0) {
            throw new InvalidTransaction(env.getProperty("TRANSACTION.INVALID.TRANSACTION.EXCEPTION.MESSAGE"));
        }
        toAccount.setBalance(fromAccountBal + transAmt);
        fromAccount.setBalance(toAccountBal - transAmt);
        accountService.updateAccountBalance(toAccount, token);
        accountService.updateAccountBalance(fromAccount, token);
        Transaction newTransaction = new Transaction();
        newTransaction.setTransAmount(transDto.getTransAmount());
        newTransaction.setTransType(transDto.getTransType());
        newTransaction.setFromAccount(fromAccount);
        newTransaction.setToAcct(transDto.getToAcct());
        transactionRepo.save(newTransaction);
        return "Trasnfer Successful";

    }

    public List<Transaction> getTransactionByAccountNumber(long accountNumber, String token) {
        Account existingAccount = accountService.getAccountById(accountNumber, token);
        return transactionRepo.findByFromAccount(existingAccount).get();
    }
}
