package com.base.BaseDependencies.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.PaidBillsDto;
import com.base.BaseDependencies.Dtos.TransactionDto;
import com.base.BaseDependencies.Dtos.RequestDtos.PayBillRequestDto;
import com.base.BaseDependencies.Dtos.RequestDtos.TransferRequestDto;
import com.base.BaseDependencies.Service.BillService;
import com.base.BaseDependencies.Service.LoansService;
import com.base.BaseDependencies.Service.TransactionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private TransactionService transactionService;
    private BillService billService;
    private LoansService loansService;

    @PostMapping("/outer-bank")
    public ResponseEntity<String> outerTransfer(@RequestBody TransferRequestDto request,
            @RequestHeader("Authorization") String userToken) {

        String transferTransaction = transactionService.outerBankTransfer(request, userToken);
        return new ResponseEntity<>(transferTransaction, HttpStatus.OK);

    }

    @PostMapping("/inner-bank")
    public ResponseEntity<String> innerTransfer(@RequestBody TransferRequestDto request,
            @RequestHeader("Authorization") String userToken) {

        String response = transactionService.innerBankTransfer(request, userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<TransactionDto>> trasnfer(@PathVariable long accountId,
            @RequestHeader("Authorization") String userToken) {

        List<TransactionDto> response = transactionService.getTransactionByAccountNumber(accountId, userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/recent")
    public ResponseEntity<List<TransactionDto>> getRecentTransactions(
            @RequestHeader("Authorization") String userToken) {

        List<TransactionDto> response = transactionService.getAllRecentTransactionsByAccount(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/bill")
    public ResponseEntity<List<PaidBillsDto>> getMethodName(@RequestHeader("Authorization") String userToken) {
        List<PaidBillsDto> response = billService.getClientPaidBills(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/bill")
    public ResponseEntity<String> postMethodName(@RequestBody PayBillRequestDto request,
            @RequestHeader("Authorization") String userToken) {
        String response = billService.payBill(request, userToken);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/loan")
    public ResponseEntity<String> payLoan(@RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String userToken) {
        String response = loansService.payLoan(request, userToken);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
