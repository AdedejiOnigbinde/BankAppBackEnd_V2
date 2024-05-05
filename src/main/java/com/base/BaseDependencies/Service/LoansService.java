package com.base.BaseDependencies.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.LoanDto;
import com.base.BaseDependencies.Dtos.RequestDtos.LoanRequestDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.AccountNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.LoanNotFound;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.Loan;
import com.base.BaseDependencies.Models.LoanRequest;
import com.base.BaseDependencies.Repository.AccountRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Repository.LoansRepo;
import com.base.BaseDependencies.Repository.LoansRequestRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LoansService {
    private AccountRepo accountRepo;
    private ClientRepo clientRepo;
    private LoansRepo loansRepo;
    private LoansRequestRepo loansRequestRepo;
    private JwtManager tokenManager;
    private ModelMapper modelMapper;

    public LoanDto createLoanRequest(LoanRequestDto request, String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));

        Loan newLoan = Loan.builder()
                .amount(request.getAmount())
                .installment(request.getInstallment())
                .duration(request.getDuration())
                .interestRate(request.getInterestRate())
                .status(GeneralMessageConstants.IN_REVIEW_STATUS)
                .paidAmount(0)
                .loanOwner(existingClient)
                .build();
        Loan savedLoan = loansRepo.save(newLoan);

        LoanRequest newRequest = LoanRequest.builder()
                .amount(request.getAmount())
                .installment(request.getInstallment())
                .duration(request.getDuration())
                .interestRate(request.getInterestRate())
                .status(GeneralMessageConstants.IN_REVIEW_STATUS)
                .requestDate(LocalDateTime.now())
                .loanRequestOwner(existingClient)
                .loanId(savedLoan.getLoanId())
                .build();

        loansRequestRepo.save(newRequest);

        LoanDto response = modelMapper.map(savedLoan, LoanDto.class);
        return response;
    }

    public String changeLoanStatus(Map<String, String> request, String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        if (existingClient.getRoles().contains("ADMIN")) {
            int loanRequestId = Integer.parseInt(request.get("loanId"));
            String changeStatus = request.get("changeStatus");
            LoanRequest loanRequest = loansRequestRepo.findById(loanRequestId)
                    .orElseThrow(() -> new LoanNotFound(ErrorMessageConstants.LOAN_NOT_FOUND_EXCEPTION_MESSAGE));
            Loan foundLoan = loansRepo.findById(loanRequest.getLoanId())
                    .orElseThrow(() -> new LoanNotFound(ErrorMessageConstants.LOAN_NOT_FOUND_EXCEPTION_MESSAGE));
            if (changeStatus.equalsIgnoreCase(GeneralMessageConstants.APPROVE_STATUS)) {
                List<Account> requestorAccounts = loanRequest.getLoanRequestOwner().getAccounts();
                Account checkingAccount = determineCheckingAccount(requestorAccounts);
                checkingAccount.setBalance(checkingAccount.getBalance() + foundLoan.getAmount());
                accountRepo.save(checkingAccount);
            }
            loanRequest.setStatus(changeStatus);
            foundLoan.setStatus(changeStatus);
            foundLoan.setApprovalDate(LocalDateTime.now());
            loansRequestRepo.save(loanRequest);
            loansRepo.save(foundLoan);

        } else {
            throw new InvalidTransaction(ErrorMessageConstants.UAUTHORIZED_REQUEST_EXCEPTION_MESSAGE);
        }

        return GeneralMessageConstants.SUCCESSFUL_LOAN_APPROVAL_MESSAGE;
    }

    public List<LoanDto> getAllClientLoans(String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Optional<List<Loan>> loans = loansRepo.findByLoanOwner(existingClient);
        if (loans.isPresent()) {
            return loans.get().stream().map(loan -> modelMapper.map(loan, LoanDto.class)).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public LoanDto getClientLoan(int loanId, String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Loan foundLoan = loansRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFound(ErrorMessageConstants.LOAN_NOT_FOUND_EXCEPTION_MESSAGE));
        if (!foundLoan.getLoanOwner().equals(existingClient)) {
            throw new LoanNotFound(ErrorMessageConstants.LOAN_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return modelMapper.map(foundLoan, LoanDto.class);
    }

    public String payLoan(Map<String, String> request, String token) {
        String getClientUserName = tokenManager.parseToken(token);
        int loanId = Integer.parseInt(request.get("loanId"));
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Loan foundLoan = loansRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFound(ErrorMessageConstants.LOAN_NOT_FOUND_EXCEPTION_MESSAGE));
        double paymentAmount = Double.parseDouble(request.get("paymentAmount"));
        if (!foundLoan.getLoanOwner().equals(existingClient)) {
            throw new LoanNotFound(ErrorMessageConstants.LOAN_NOT_FOUND_EXCEPTION_MESSAGE);
        } else if (foundLoan.getPaidAmount() < (foundLoan.getPaidAmount() + paymentAmount)) {
            throw new InvalidTransaction("You Cannot Over Pay A Loan");
        } else if (foundLoan.getStatus().equals(GeneralMessageConstants.IN_REVIEW_STATUS)
                || foundLoan.getStatus().equals(GeneralMessageConstants.REJECTED_STATUS)) {
            throw new InvalidTransaction("You Cannot Over Pay A Loan That Hasnt Been Approved");
        }
        Account checkingAccount = determineCheckingAccount(existingClient.getAccounts());
        checkingAccount.setBalance(checkingAccount.getBalance() - paymentAmount);
        foundLoan.setPaidAmount(foundLoan.getPaidAmount() + paymentAmount);
        accountRepo.save(checkingAccount);
        loansRepo.save(foundLoan);

        return GeneralMessageConstants.SUCCESSFUL_LOAN_PAYMENT_MESSAGE;
    }

    public List<LoanRequestDto> getAllClientLoanRequest(String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        if (existingClient.getRoles().get(0).getRoleName().equals("ADMIN")) {
            Optional<List<LoanRequest>> requestList = loansRequestRepo
                    .findByStatus(GeneralMessageConstants.IN_REVIEW_STATUS);
            if (requestList.isPresent()) {
                return requestList.stream().map(loan -> modelMapper.map(loan, LoanRequestDto.class))
                        .collect(Collectors.toList());
            }

        } else {
            throw new InvalidTransaction(ErrorMessageConstants.UAUTHORIZED_REQUEST_EXCEPTION_MESSAGE);
        }

        return Collections.emptyList();
    }

    private Account determineCheckingAccount(List<Account> senderAccountList) {
        for (Account account : senderAccountList) {
            if (GeneralMessageConstants.CHECKINGS_ACCOUNT.equalsIgnoreCase(account.getAccountType())) {
                return account;
            }
        }
        throw new AccountNotFound(ErrorMessageConstants.LOAN_APPROVAL_ACCOUNT_MESSAGE);
    }
}
