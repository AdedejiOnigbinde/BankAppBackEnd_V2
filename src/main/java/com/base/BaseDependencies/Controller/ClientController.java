package com.base.BaseDependencies.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
@CrossOrigin("http://localhost:4200")
public class ClientController {

    private final ClientService clientService;
    private final BeneficiaryService beneficiaryService;
    private final BillService billService;
    private final LoansService loansService;

    @GetMapping("/all")
    public ResponseEntity<List<ClientDto>> getAllClient() {
        List<ClientDto> response = clientService.getAllClients();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<HttpStatus> deleteClient(@RequestHeader("Authorization") String userToken) {
        clientService.deleteClient(userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/beneficiary/{beneficiaryId}")
    public ResponseEntity<HttpStatus> deleteBeneficiary(@PathVariable int beneficiaryId,
            @RequestHeader("Authorization") String userToken) {
        beneficiaryService.deleteBeneficiary(beneficiaryId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/beneficiary")
    public ResponseEntity<List<BeneficiaryDto>> getAllBeneficiaries(@RequestHeader("Authorization") String userToken) {
        List<BeneficiaryDto> response = beneficiaryService.getAllClientBeneficiaries(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/bill")
    public ResponseEntity<List<BillDto>> getAllBills(@RequestHeader("Authorization") String userToken) {
        List<BillDto> response = billService.getAllClientBills(userToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/bill/{billId}")
    public ResponseEntity<HttpStatus> deleteBill(@PathVariable int billId,
            @RequestHeader("Authorization") String userToken) {
        billService.deleteBill(billId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/profile")
    public ResponseEntity<ClientDto> updateProfile(@RequestBody ClientDto request,
            @RequestHeader("Authorization") String token) {
        ClientDto response = clientService.updateProfile(request, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ClientDto> getProfile(@RequestHeader("Authorization") String token) {
        ClientDto response = clientService.getProfile(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/profile/password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDto request,
            @RequestHeader("Authorization") String token) {
        String response = clientService.changePassword(request, token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/loan")
    public ResponseEntity<LoanDto> applyForLoan(@RequestBody LoanRequestDto request,
            @RequestHeader("Authorization") String token) {

        LoanDto response = loansService.createLoanRequest(request, token);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/loan/status")
    public ResponseEntity<String> changeLoanStatus(@RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {
        String response = loansService.changeLoanStatus(request, token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("loan")
    public ResponseEntity<List<LoanDto>> getAllClientLoans(@RequestHeader("Authorization") String token) {
        List<LoanDto> response = loansService.getAllClientLoans(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("loan/{loanId}")
    public ResponseEntity<LoanDto> getClientLoan(@RequestHeader("Authorization") String token,
            @PathVariable int loanId) {
        LoanDto response = loansService.getClientLoan(loanId, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("loan/admin")
    public ResponseEntity<List<LoanRequestDto>> getAllClientRequest(@RequestHeader("Authorization") String token) {
        List<LoanRequestDto> response = loansService.getAllClientLoanRequest(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
