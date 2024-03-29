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

import com.base.BaseDependencies.Dtos.TransactionDto;
import com.base.BaseDependencies.Models.Transaction;
import com.base.BaseDependencies.Service.TransactionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
@CrossOrigin("http://localhost:4200")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(@RequestBody TransactionDto transactionDto,
            @RequestHeader("Authorization") String token) {

        String witdrawalTransaction = transactionService.withdrawal(transactionDto, token);
        return new ResponseEntity<>(witdrawalTransaction, HttpStatus.OK);

    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionDto transactionDto,
            @RequestHeader("Authorization") String token) {

        String depositTransaction = transactionService.deposit(transactionDto, token);
        return new ResponseEntity<>(depositTransaction, HttpStatus.OK);

    }

    @PostMapping("/transfer")
    public ResponseEntity<String> trasnfer(@RequestBody TransactionDto transactionDto,
            @RequestHeader("Authorization") String token) {

        String transferTransaction = transactionService.transfer(transactionDto, token);
        return new ResponseEntity<>(transferTransaction, HttpStatus.OK);

    }

    @GetMapping("/accounttransactions/{accountId}")
    public ResponseEntity<List<Transaction>> trasnfer(@PathVariable long accountId,
            @RequestHeader("Authorization") String token) {

        List<Transaction> transactionList = transactionService.getTransactionByAccountNumber(accountId, token);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);

    }

    @GetMapping("/accountrecenttransactions")
    public ResponseEntity<List<Transaction>> getRecentTransactions(@RequestHeader("Authorization") String token) {

        List<Transaction> recentTransactionList = transactionService.getAllRecentTransactionsByUser(token);
        return new ResponseEntity<>(recentTransactionList, HttpStatus.OK);

    }

}
