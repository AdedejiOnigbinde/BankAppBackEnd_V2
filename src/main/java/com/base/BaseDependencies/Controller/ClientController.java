package com.base.BaseDependencies.Controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.BeneficiaryDto;
import com.base.BaseDependencies.Dtos.BillDto;
import com.base.BaseDependencies.Dtos.ClientDto;
import com.base.BaseDependencies.Dtos.LoanDto;
import com.base.BaseDependencies.Dtos.RequestDtos.ChangePasswordRequestDto;
import com.base.BaseDependencies.Dtos.RequestDtos.LoanRequestDto;
import com.base.BaseDependencies.Service.BeneficiaryService;
import com.base.BaseDependencies.Service.BillService;
import com.base.BaseDependencies.Service.ClientService;
import com.base.BaseDependencies.Service.LoansService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final BeneficiaryService beneficiaryService;
    private final BillService billService;
    private final LoansService loansService;

    @GetMapping("/all")
    public ResponseEntity<Page<ClientDto>> getAllClient(@RequestParam(defaultValue = "0") int page) {
        Page<ClientDto> response = clientService.getAllClients(page);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<HttpStatus> deleteClient(
            @CookieValue(name = "accessToken", required = false) String userToken) {
        clientService.deleteClient(userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/beneficiary/{beneficiaryId}")
    public ResponseEntity<HttpStatus> deleteBeneficiary(@PathVariable int beneficiaryId,
            @CookieValue(name = "accessToken", required = false) String userToken) {
        beneficiaryService.deleteBeneficiary(beneficiaryId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/beneficiary")
    public ResponseEntity<List<BeneficiaryDto>> getAllBeneficiaries(
            @CookieValue(name = "accessToken", required = false) String userToken) {
        List<BeneficiaryDto> response = beneficiaryService.getAllClientBeneficiaries(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/bill")
    public ResponseEntity<List<BillDto>> getAllBills(
            @CookieValue(name = "accessToken", required = false) String userToken) {
        List<BillDto> response = billService.getAllClientBills(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/bill/{billId}")
    public ResponseEntity<HttpStatus> deleteBill(@PathVariable int billId,
            @CookieValue(name = "accessToken", required = false) String userToken) {
        billService.deleteBill(billId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/profile")
    public ResponseEntity<ClientDto> updateProfile(@Valid @RequestBody ClientDto request,
            @CookieValue(name = "accessToken", required = false) String userToken) {
        ClientDto response = clientService.updateProfile(request, userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ClientDto> getProfile(
            @CookieValue(name = "accessToken", required = false) String userToken) {
        ClientDto response = clientService.getProfile(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/profile/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto request,
            @CookieValue(name = "accessToken", required = false) String userToken) {
        String response = clientService.changePassword(request, userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/loan")
    public ResponseEntity<LoanDto> applyForLoan(@Valid @RequestBody LoanRequestDto request,
            @CookieValue(name = "accessToken", required = false) String userToken) {
        LoanDto response = loansService.createLoanRequest(request, userToken);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/loan/status")
    public ResponseEntity<String> changeLoanStatus(@RequestBody Map<String, String> request,
            @CookieValue(name = "accessToken", required = false) String userToken) {
        String response = loansService.changeLoanStatus(request, userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("loan")
    public ResponseEntity<List<LoanDto>> getAllClientLoans(
            @CookieValue(name = "accessToken", required = false) String userToken) {
        List<LoanDto> response = loansService.getAllClientLoans(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("loan/sum")
    public ResponseEntity<Double> getClientsLoanSum(
            @CookieValue(name = "accessToken", required = false) String userToken) {
        Double response = loansService.getTotalLoanSum(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("loan/{loanId}")
    public ResponseEntity<LoanDto> getClientLoan(
            @CookieValue(name = "accessToken", required = false) String userToken,
            @PathVariable int loanId) {
        LoanDto response = loansService.getClientLoan(loanId, userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("loan/admin")
    public ResponseEntity<List<LoanRequestDto>> getAllClientRequest(
            @CookieValue(name = "accessToken", required = false) String userToken) {
        List<LoanRequestDto> response = loansService.getAllClientLoanRequest(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
