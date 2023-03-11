package com.base.BaseDependencies.Controller;

import java.util.List;

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
            @RequestHeader("userToekn") String userToken) {
        try {
            Account newAccount = accountService.createAccount(accountDto, userToken);
            return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    // @GetMapping("/")
    // public ResponseEntity<?> loginClient(@RequestBody LoginClientDto loginClientDto) {
    //     try {
    //         String verifiedClient = clientService.verifyClient(loginClientDto);
    //         return new ResponseEntity<>(verifiedClient, HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    //     }

    // }

    @GetMapping("/allaccounts")
    public ResponseEntity<?> getAllAccounts() {
        try {
            List<Account> accountList = accountService.getAllAccounts();
            return new ResponseEntity<>(accountList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/deleteaccount/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId, @RequestHeader("userToken") String userToken) {
        try {
            accountService.deleteAccount(accountId, userToken);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
