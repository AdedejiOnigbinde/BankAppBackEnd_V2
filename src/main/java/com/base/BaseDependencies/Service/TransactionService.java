package com.base.BaseDependencies.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
            accountService.updateAccountBalance(updatedAccount);
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
        accountService.updateAccountBalance(updatedAccount);
        Transaction newTransaction = new Transaction();
        newTransaction.setTransAmount(transDto.getTransAmount());
        newTransaction.setTransType(transDto.getTransType());
        newTransaction.setFromAccount(updatedAccount);
        transactionRepo.save(newTransaction);
        return "Deposit Successful";
    }

    public String transfer(TransactionDto transDto, String token) {
        List<Account> accountList = accountService.getTwoAccountsById(transDto.getFromAcct(),transDto.getToAcct(),token);
        Account fromAccount = accountList.get(0);
        Account toAccount = accountList.get(1);
        double fromAccountBal = fromAccount.getBalance();
        double transAmt = transDto.getTransAmount();
        double toAccountBal = toAccount.getBalance();
        if (fromAccountBal < transAmt) {
            throw new InsufficentFunds(env.getProperty("TRANSACTION.INSUFFICIENT.FUNDS.EXCEPTION.MESSAGE"));
        } else if (fromAccountBal < 0 && transAmt < 0) {
            throw new InvalidTransaction(env.getProperty("TRANSACTION.INVALID.TRANSACTION.EXCEPTION.MESSAGE"));
        }
        toAccount.setBalance(toAccountBal + transAmt);
        fromAccount.setBalance(fromAccountBal - transAmt);
        accountService.updateAccountBalance(toAccount);
        accountService.updateAccountBalance(fromAccount);
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

    public List<Transaction> getAllRecentTransactionsByUser(String token) {
        List<Transaction> recentTransactions = new ArrayList<>();
        List<Account> accountList = accountService.getAccountByClientUserName(token);

        recentTransactions = accountList.stream()
                .flatMap(account -> getTransactionByAccountNumber(account.getAccountNumber(), token).stream())
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .limit(5)
                .collect(Collectors.toList());
        return recentTransactions;
    }
}
