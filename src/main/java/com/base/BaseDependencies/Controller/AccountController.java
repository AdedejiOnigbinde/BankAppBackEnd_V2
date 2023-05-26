package com.base.BaseDependencies.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin("http://localhost:4200")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto,
            @RequestHeader("Authorization") String userToken) {
        Account newAccount = accountService.createAccount(accountDto, userToken);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);

    }

    @GetMapping("/allaccounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accountList = accountService.getAllAccounts();
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    @GetMapping("/alluseraccounts")
    public ResponseEntity<List<Account>> getAccountsByUserName(@RequestHeader("Authorization") String userToken) {
        List<Account> accountList = accountService.getAccountByClientUserName(userToken);
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    @GetMapping("/useraccount/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId, @RequestHeader("Authorization") String userToken) {
       Account account =  accountService.getAccountById(accountId, userToken);
        return new ResponseEntity<>(account, HttpStatus.OK);

    }

    @DeleteMapping("/deleteaccount/{accountId}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable Long accountId, @RequestHeader("Authorization") String userToken) {
        accountService.deleteAccount(accountId, userToken);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
