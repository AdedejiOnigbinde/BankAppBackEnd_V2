package com.base.BaseDependencies.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.BillDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BillNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.Models.Bill;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Repository.BillRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BillService {

    private BillRepo billRepo;
    private ClientRepo clientRepo;
    private JwtManager tokenManager;
    private ModelMapper modelMapper;

    public String saveBill(String category, String biller, String nickname, Client client) {
        Bill newBill = Bill.builder()
                .category(category)
                .biller(biller)
                .nickname(nickname)
                .billOwner(client)
                .build();

        billRepo.save(newBill);
        return GeneralMessageConstants.SUCCESSFUL_BILL_SAVE_MESSAGE;
    }

    public List<BillDto> getAllClientBills(String token) {
        List<BillDto> bills = new ArrayList<>();
        String ownerUserName = tokenManager.parseToken(token);
        Client client = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Optional<List<Bill>> billsList = billRepo.findByBillOwner(client);
        if (!billsList.isEmpty()) {
            billsList.get().forEach(bill -> {
                BillDto mappedBillDto = modelMapper.map(bill, BillDto.class);
                bills.add(mappedBillDto);
            });
        }

        return bills;
    }

    public String deleteBill(int billId, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client client = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Bill bill = billRepo.findById(billId).orElseThrow(() -> new BillNotFound(ErrorMessageConstants.BILL_NOT_FOUND_EXCEPTION_MESSAGE));
        if(!client.getBills().contains(bill)){
            throw new BillNotFound(ErrorMessageConstants.BILL_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        client.getBills().remove(bill);
        bill.setBillOwner(null);
        billRepo.deleteById(billId);

        return GeneralMessageConstants.SUCCESSFUL_BILL_DELETION_MESSAGE;
    }
}
