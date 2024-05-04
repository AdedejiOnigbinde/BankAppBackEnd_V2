package com.base.BaseDependencies.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.BeneficiaryDto;
import com.base.BaseDependencies.Dtos.BillDto;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Service.BeneficiaryService;
import com.base.BaseDependencies.Service.BillService;
import com.base.BaseDependencies.Service.ClientService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
@CrossOrigin("http://localhost:4200")
public class ClientController {

    private final ClientService clientService;
    private final BeneficiaryService beneficiaryService;
    private final BillService billService;

    @GetMapping("/allclients")
    public ResponseEntity<List<Client>> getAllClient() {
        List<Client> clientList = clientService.getAllClients();
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }

    @DeleteMapping("/removeclient")
    public ResponseEntity<HttpStatus> deleteClient(@RequestHeader("Authorization") String userToken) {
        clientService.deleteClient(userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/removebeneficiary/{beneficiaryId}")
    public ResponseEntity<HttpStatus> deleteBeneficiary(@PathVariable int beneficiaryId,
            @RequestHeader("Authorization") String userToken) {
        beneficiaryService.deleteBeneficiary(beneficiaryId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/allbeneficiaries")
    public ResponseEntity<List<BeneficiaryDto>> getAllBeneficiaries(@RequestHeader("Authorization") String userToken) {
        List<BeneficiaryDto> beneficiariesList = beneficiaryService.getAllClientBeneficiaries(userToken);
        return new ResponseEntity<>(beneficiariesList, HttpStatus.OK);
    }

    @GetMapping("/allbills")
    public ResponseEntity<List<BillDto>> getAllBills(@RequestHeader("Authorization") String userToken) {
        List<BillDto> billList = billService.getAllClientBills(userToken);
        return new ResponseEntity<>(billList, HttpStatus.OK);
    }

    @DeleteMapping("/removebill/{billId}")
    public ResponseEntity<HttpStatus> deleteBill(@PathVariable int billId,
            @RequestHeader("Authorization") String userToken) {
        billService.deleteBill(billId, userToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
