package com.base.BaseDependencies.Service;

import java.util.ArrayList;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.TransactionDto;
import com.base.BaseDependencies.Dtos.RequestDtos.TransferRequestDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InsufficentFunds;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.DepositRequest;
import com.base.BaseDependencies.Models.Transaction;
import com.base.BaseDependencies.Repository.AccountRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Repository.TransactionRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TransactionService {
    private TransactionRepo transactionRepo;
    private AccountRepo accountRepo;
    private ClientRepo clientRepo;
    private JwtManager tokenManager;
    private BeneficiaryService beneficiaryService;

    public String deposit(DepositRequest depositRequest, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        if (getClient.getRoles().contains("ADMIN")) {
            Account updatedAccount = accountRepo.findById(
                    depositRequest.getDepositAccount().getAccountNumber())
                    .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));

            double accountBal = updatedAccount.getBalance();
            double depositAmt = depositRequest.getCheckAmount();
            updatedAccount.setBalance(accountBal + depositAmt);
            Account savedAccount = accountRepo.save(updatedAccount);
            Transaction newTransaction = Transaction.builder()
                    .transAmount(depositAmt)
                    .transType(GeneralMessageConstants.DEPOSIT)
                    .fromAccount(updatedAccount)
                    .status(GeneralMessageConstants.SUCCESS_STATUS)
                    .balance(savedAccount.getBalance())
                    .toAcct(1L)
                    .build();
            transactionRepo.save(newTransaction);
            return GeneralMessageConstants.SUCCESSFUL_DEPOSIT_MESSAGE;
        } else {
            throw new InvalidTransaction(ErrorMessageConstants.UAUTHORIZED_REQUEST_EXCEPTION_MESSAGE);
        }

    }

    public String outerBankTransfer(TransferRequestDto transDto, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));

        List<Account> senderAccountList = getClient.getAccounts();
        if (getClient.getPinNumber() != transDto.getPin()) {
            throw new InvalidTransaction(ErrorMessageConstants.INVALID_PIN_EXCEPTION_MESSAGE);
        } else if (GeneralMessageConstants.HANDLE_MONEY.equalsIgnoreCase(transDto.getBank())) {
            Account toAccount = accountRepo.findById(transDto.getToAcct())
                    .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));
            return tranferbetweenTwoAccounts(senderAccountList, toAccount, transDto);
        } else {
            return defaultTransfer(senderAccountList, transDto);
        }

    }

    public String innerBankTransfer(TransferRequestDto transDto, String token) {
        Account fromAccount = accountRepo.findById(transDto.getFromacct())
                .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));
        if (!verifyUserAccount(fromAccount, token, transDto.getPin())) {
            throw new InvalidTransaction(ErrorMessageConstants.UAUTHORIZED_REQUEST_EXCEPTION_MESSAGE);
        }
        Account toAccount = accountRepo.findById(transDto.getToAcct())
                .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));

        double fromAccountBal = fromAccount.getBalance();
        double transAmt = transDto.getAmount();
        double toAccountBal = toAccount.getBalance();
        if (fromAccountBal < transAmt) {
            throw new InsufficentFunds(ErrorMessageConstants.TRANSACTION_INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE);
        } else if (fromAccountBal < 0 && transAmt < 0) {
            throw new InvalidTransaction(ErrorMessageConstants.TRANSACTION_INVALID_TRANSACTION_EXCEPTION_MESSAGE);
        }
        toAccount.setBalance(toAccountBal + transAmt);
        fromAccount.setBalance(fromAccountBal - transAmt);
        accountRepo.save(toAccount);
        Account savedAccount = accountRepo.save(fromAccount);
        Transaction newTransaction = Transaction.builder()
                .balance(savedAccount.getBalance())
                .fromAccount(fromAccount)
                .toAcct(transDto.getToAcct())
                .transType(GeneralMessageConstants.TRANSFER)
                .transAmount(transDto.getAmount())
                .status(GeneralMessageConstants.SUCCESS_STATUS)
                .build();
        transactionRepo.save(newTransaction);
        return GeneralMessageConstants.SUCCESSFUL_TRANSFER_MESSAGE;
    }

    // Edit function to get pageable transaction by transaction type
    public List<TransactionDto> getTransactionByAccountNumber(long accountNumber, String token) {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        Account foundAccount = accountRepo.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));

        if (!verifyUserAccount(foundAccount, token, 0)) {
            throw new InvalidTransaction(ErrorMessageConstants.UAUTHORIZED_REQUEST_EXCEPTION_MESSAGE);
        }
        foundAccount.getTransaction().forEach(transaction -> {
            TransactionDto transactionDto = mapTransactionToDto(transaction);
            transactionDtos.add(transactionDto);
        });
        return transactionDtos;
    }

    public List<TransactionDto> getAllRecentTransactionsByAccount(String token) {
        List<TransactionDto> transactions = new ArrayList<>();
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        List<Account> account = accountRepo.findByOwnerId(getClient)
                .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));
        Pageable pageable = PageRequest.of(0, 5, Sort.by("transactionDate").descending());
        List<Transaction> recentTransactions = transactionRepo
                .findByFromAccountOrderByTransactionDateDesc(account.get(0), pageable)
                .orElseThrow(() -> new AccountNotFound(ErrorMessageConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));
        recentTransactions.forEach(transaction -> {
            TransactionDto mappedTransaction = mapTransactionToDto(transaction);
            transactions.add(mappedTransaction);
        });
        return transactions;
    }

    private String tranferbetweenTwoAccounts(List<Account> senderAccountList, Account reciversAccount,
            TransferRequestDto transDto) {
        String response = "";
        String savedBeneficiaryResponse = "";
        Account sendersAccount = determineCheckingAccount(senderAccountList);

        if (sendersAccount != null) {

            double sendersAccountBal = sendersAccount.getBalance();
            double transAmt = transDto.getAmount();
            double reciversAccountBal = reciversAccount.getBalance();

            if (sendersAccountBal < transAmt) {
                throw new InsufficentFunds(ErrorMessageConstants.TRANSACTION_INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE);
            } else if (sendersAccountBal < 0 && transAmt < 0) {
                throw new InvalidTransaction(ErrorMessageConstants.TRANSACTION_INVALID_TRANSACTION_EXCEPTION_MESSAGE);
            } else if (sendersAccount.getDailyTransferLimit() < sendersAccount.getCalcLimit() + transAmt) {
                throw new InvalidTransaction(ErrorMessageConstants.DAILY_TRANSACTION_LIMIT_EXCEPTION_MESSAGE);
            }
            reciversAccount.setBalance(reciversAccountBal + transAmt);
            sendersAccount.setBalance(sendersAccountBal - transAmt);
            sendersAccount.setCalcLimit(sendersAccount.getCalcLimit() + transAmt);
            accountRepo.save(reciversAccount);
            Account savedAccount = accountRepo.save(sendersAccount);
            Transaction newTransaction = Transaction.builder()
                    .balance(savedAccount.getBalance())
                    .fromAccount(sendersAccount)
                    .toAcct(transDto.getToAcct())
                    .transType(GeneralMessageConstants.TRANSFER)
                    .transAmount(transDto.getAmount())
                    .status(GeneralMessageConstants.SUCCESS_STATUS)
                    .build();
            transactionRepo.save(newTransaction);
            if (transDto.isAddBeneficary()) {
                savedBeneficiaryResponse = beneficiaryService.createBeneficiary(reciversAccount.getAccountNumber(),
                        transDto.getBank(),
                        sendersAccount.getOwnerId());

            }

            response = savedBeneficiaryResponse.length() > 1
                    ? GeneralMessageConstants.SUCCESSFUL_TRANSFER_MESSAGE + " And " + savedBeneficiaryResponse
                    : GeneralMessageConstants.SUCCESSFUL_TRANSFER_MESSAGE;
            return response;

        } else {
            throw new InvalidTransaction(ErrorMessageConstants.TRANSACTION_TRANSFER_EXCEPTION_MESSAGE);
        }
    }

    private String defaultTransfer(List<Account> senderAccountList, TransferRequestDto transDto) {
        String response = "";
        String savedBeneficiaryResponse = "";
        Account senderAccount = determineCheckingAccount(senderAccountList);
        if (senderAccount == null) {
            throw new InvalidTransaction(ErrorMessageConstants.TRANSACTION_TRANSFER_EXCEPTION_MESSAGE);
        } else if (senderAccount.getBalance() < transDto.getAmount()) {
            throw new InsufficentFunds(ErrorMessageConstants.TRANSACTION_INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE);
        } else if (senderAccount.getDailyTransferLimit() < senderAccount.getCalcLimit() + transDto.getAmount()) {
            throw new InvalidTransaction(ErrorMessageConstants.DAILY_TRANSACTION_LIMIT_EXCEPTION_MESSAGE);
        } else {
            senderAccount.setBalance(senderAccount.getBalance() - transDto.getAmount());
            senderAccount.setCalcLimit(senderAccount.getCalcLimit() + transDto.getAmount());
            Account savedAccount = accountRepo.save(senderAccount);
            Transaction newTransaction = Transaction.builder()
                    .transAmount(transDto.getAmount())
                    .transType(GeneralMessageConstants.TRANSFER)
                    .fromAccount(senderAccount)
                    .toAcct(transDto.getToAcct())
                    .balance(savedAccount.getBalance())
                    .status(GeneralMessageConstants.SUCCESS_STATUS)
                    .build();
            transactionRepo.save(newTransaction);
            if (transDto.isAddBeneficary()) {
                savedBeneficiaryResponse = beneficiaryService.createBeneficiary(transDto.getToAcct(),
                        transDto.getBank(),
                        senderAccount.getOwnerId());

            }
            response = savedBeneficiaryResponse.length() > 1
                    ? GeneralMessageConstants.SUCCESSFUL_TRANSFER_MESSAGE + " And " + savedBeneficiaryResponse
                    : GeneralMessageConstants.SUCCESSFUL_TRANSFER_MESSAGE;
            return response;
        }
    }

    private TransactionDto mapTransactionToDto(Transaction transaction) {
        TransactionDto transactionDto = TransactionDto.builder()
                .toAcct(transaction.getToAcct())
                .balance(transaction.getBalance())
                .fromAcct(transaction.getFromAccount().getAccountNumber())
                .transAmount(transaction.getTransAmount())
                .status(transaction.getStatus())
                .transType(transaction.getTransType())
                .transactionDate(transaction.getTransactionDate())
                .build();
        return transactionDto;
    }

    private boolean verifyUserAccount(Account account, String token, int pin) {
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        if (pin != 0) {
            return getClient.getPinNumber() == pin && getClient.getAccounts().contains(account);
        }
        return getClient.getAccounts().contains(account);
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
