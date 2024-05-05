package com.base.BaseDependencies.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.base.BaseDependencies.Service.PaidBillsService;
import com.base.BaseDependencies.Service.TransactionService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
@CrossOrigin("http://localhost:4200")
public class TransactionController {

    private TransactionService transactionService;
    private PaidBillsService paidBillsService;

    @PostMapping("/outer-bank-transfer")
    public ResponseEntity<String> outerTransfer(@RequestBody TransferRequestDto transactionDto,
            @RequestHeader("Authorization") String token) {

        String transferTransaction = transactionService.outerBankTransfer(transactionDto, token);
        return new ResponseEntity<>(transferTransaction, HttpStatus.OK);

    }

    @PostMapping("/inner-bank-transfer")
    public ResponseEntity<String> innerTransfer(@RequestBody TransferRequestDto transactionDto,
            @RequestHeader("Authorization") String token) {

        String transferTransaction = transactionService.innerBankTransfer(transactionDto, token);
        return new ResponseEntity<>(transferTransaction, HttpStatus.OK);

    }

    @GetMapping("/accounttransactions/{accountId}")
    public ResponseEntity<List<TransactionDto>> trasnfer(@PathVariable long accountId,
            @RequestHeader("Authorization") String token) {

        List<TransactionDto> transactionList = transactionService.getTransactionByAccountNumber(accountId, token);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);

    }

    @GetMapping("/accountrecenttransactions")
    public ResponseEntity<List<TransactionDto>> getRecentTransactions(@RequestHeader("Authorization") String token) {

        List<TransactionDto> recentTransactionList = transactionService.getAllRecentTransactionsByAccount(token);
        return new ResponseEntity<>(recentTransactionList, HttpStatus.OK);

    }

    @GetMapping("/allpaidbills")
    public ResponseEntity<List<PaidBillsDto>> getMethodName(@RequestHeader("Authorization") String token) {
        List<PaidBillsDto> paidBillsList = paidBillsService.getClientPaidBills(token);
        return new ResponseEntity<>(paidBillsList, HttpStatus.OK);
    }

    @PostMapping("/paybill")
    public ResponseEntity<String> postMethodName(@RequestBody PayBillRequestDto request,
            @RequestHeader("Authorization") String token) {
        String response = paidBillsService.payBill(request, token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
