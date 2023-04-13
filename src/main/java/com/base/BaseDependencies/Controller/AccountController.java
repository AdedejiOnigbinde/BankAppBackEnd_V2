package com.base.BaseDependencies.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.AccountDto;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Service.AccountService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto,
            @RequestHeader("Authorization") String userToken) {
        Account newAccount = accountService.createAccount(accountDto, userToken);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);

    }

    @GetMapping("/allaccounts")
    public ResponseEntity<?> getAllAccounts() {
        List<Account> accountList = accountService.getAllAccounts();
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    @GetMapping("/alluseraccounts")
    public ResponseEntity<?> getAccountsByUserName(@RequestHeader("Authorization") String userToken) {
        List<Account> accountList = accountService.getAccountByClientUserName(userToken);
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    @GetMapping("/useraccount/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Long accountId, @RequestHeader("Authorization") String userToken) {
       Account account =  accountService.getAccountById(accountId, userToken);
        return new ResponseEntity<>(account, HttpStatus.OK);

    }

    @DeleteMapping("/deleteaccount/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId, @RequestHeader("Authorization") String userToken) {
        accountService.deleteAccount(accountId, userToken);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
