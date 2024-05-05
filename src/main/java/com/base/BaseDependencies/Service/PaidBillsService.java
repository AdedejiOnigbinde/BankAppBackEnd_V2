package com.base.BaseDependencies.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.PaidBillsDto;
import com.base.BaseDependencies.Dtos.RequestDtos.PayBillRequestDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InsufficentFunds;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidTransaction;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.PaidBills;
import com.base.BaseDependencies.Repository.AccountRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Repository.PaidBillsRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaidBillsService {
    private PaidBillsRepo paidBillsRepo;
    private JwtManager tokenManager;
    private ClientRepo clientRepo;
    private AccountRepo accountRepo;
    private BillService billService;
    private ModelMapper modelMapper;

    public String payBill(PayBillRequestDto billsDto, String token) {
        String response = "";
        String savedBillResponse = "";
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Account checkingAccount = determineCheckingAccount(getClient.getAccounts());
        if (getClient.getPinNumber() != billsDto.getPin()) {
            throw new InvalidTransaction(ErrorMessageConstants.INVALID_PIN_EXCEPTION_MESSAGE);
        } else if (checkingAccount == null) {
            throw new InvalidTransaction(ErrorMessageConstants.BILL_PAYMENT_EXCEPTION_MESSAGE);
        } else if (checkingAccount.getBalance() < billsDto.getAmount()) {
            throw new InsufficentFunds(ErrorMessageConstants.TRANSACTION_INSUFFICIENT_FUNDS_EXCEPTION_MESSAGE);
        }

        checkingAccount.setBalance(checkingAccount.getBalance() - billsDto.getAmount());
        accountRepo.save(checkingAccount);
        PaidBills newBill = PaidBills.builder()
                .payee(getClient)
                .status(GeneralMessageConstants.SUCCESS_STATUS)
                .amount(billsDto.getAmount())
                .biller(billsDto.getBiller())
                .build();

        paidBillsRepo.save(newBill);

        if (billsDto.isSaveBill()) {
            savedBillResponse = billService.saveBill(billsDto.getCategory(), billsDto.getBiller(),
                    billsDto.getNickName(), getClient);
        }
        response = savedBillResponse.length() > 1
                ? GeneralMessageConstants.SUCCESSFUL_BILL_PAID_MESSAGE + " And " + savedBillResponse
                : GeneralMessageConstants.SUCCESSFUL_BILL_PAID_MESSAGE;

        return response;
    }

    public List<PaidBillsDto> getClientPaidBills(String token) {
        List<PaidBillsDto> paidBillsList = new ArrayList<>();
        String ownerUserName = tokenManager.parseToken(token);
        Client getClient = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Optional<List<PaidBills>> paidBills = paidBillsRepo.findByPayee(getClient);
        if (paidBills.isPresent()) {
            paidBills.get().forEach(bill -> {
                PaidBillsDto mappedBill = modelMapper.map(bill, PaidBillsDto.class);
                paidBillsList.add(mappedBill);
            });
        }
        return paidBillsList;
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
