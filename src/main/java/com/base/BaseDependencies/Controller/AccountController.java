package com.base.BaseDependencies.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.AccountDto;
import com.base.BaseDependencies.Dtos.RequestDtos.DepositRequestDto;
import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Service.AccountService;
import com.base.BaseDependencies.Service.DepositRequestService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final DepositRequestService depositRequestService;

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String userToken) {
        AccountDto response = accountService.createAccount(request, userToken);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/all")
    public ResponseEntity<Page<Account>> getAllAccounts(@RequestHeader("Authorization") String userToken,
            @RequestParam(defaultValue = "0") int page) {
        Page<Account> response = accountService.getAllAccounts(page);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/client")
    public ResponseEntity<List<AccountDto>> getAccountsByUserName(@RequestHeader("Authorization") String userToken) {
        List<AccountDto> response = accountService.getAccountByClientUserName(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long accountId,
            @RequestHeader("Authorization") String userToken) {
        AccountDto response = accountService.getAccountById(accountId, userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable Long accountId,
            @RequestHeader("Authorization") String userToken) {
        accountService.deleteAccount(accountId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping("/deposit")
    public ResponseEntity<String> createDepositRequest(@RequestBody DepositRequestDto request,
            @RequestHeader("Authorization") String userToken) {
        String response = depositRequestService.createDepositRequest(request, userToken);
        return new ResponseEntity<String>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<String> editDepositRequest(@RequestBody Map<String,String> request,
            @RequestHeader("Authorization") String userToken) {
        String response = depositRequestService.deposit(request, userToken);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping("/deposit")
    public ResponseEntity<List<DepositRequestDto>> getAllClientDepositRequest(
            @RequestHeader("Authorization") String userToken) {
        List<DepositRequestDto> response = depositRequestService.getAllClientDepositRequest(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/deposit/all")
    public ResponseEntity<List<DepositRequestDto>> getAllDepositRequest() {
        List<DepositRequestDto> response = depositRequestService.getAllDepositRequest();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
