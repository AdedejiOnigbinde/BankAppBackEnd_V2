package com.base.BaseDependencies.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.BeneficiaryDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BeneficiaryExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.BeneficiaryNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.Models.Beneficiary;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Repository.BeneficiaryRepo;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BeneficiaryService {
    private BeneficiaryRepo beneficiaryRepo;
    private ClientRepo clientRepo;
    private JwtManager tokenManager;
    private ModelMapper modelMapper;

    public String createBeneficiary(long bankAccountNumber, String bank, Client client) {
        client.getBeneficiaries().forEach(beneficiary -> {
            if (beneficiary.getBankAccountNumber() == bankAccountNumber) {
                throw new BeneficiaryExists(ErrorMessageConstants.BENEFICIARY_EXISTS_EXCEPTION_MESSAGE);
            }
        });
        Beneficiary newBeneficiary = Beneficiary.builder()
                .bank(bank)
                .bankAccountNumber(bankAccountNumber)
                .beneficiaryOwner(client)
                .build();

        beneficiaryRepo.save(newBeneficiary);
        return GeneralMessageConstants.SUCCESSFUL_BENEFICIARY_SAVE_MESSAGE;
    }

    public List<BeneficiaryDto> getAllClientBeneficiaries(String token) {
        List<BeneficiaryDto> beneficiaries = new ArrayList<>();
        String ownerUserName = tokenManager.parseToken(token);
        Client client = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Optional<List<Beneficiary>> beneficiariesList = beneficiaryRepo.findByBeneficiaryOwner(client);
        if (!beneficiaries.isEmpty()) {
            beneficiariesList.get().forEach(beneficiary -> {
                BeneficiaryDto mappedBeneficiaryDto = modelMapper.map(beneficiary, BeneficiaryDto.class);
                beneficiaries.add(mappedBeneficiaryDto);
            });
        }

        return beneficiaries;
    }

    public String deleteBeneficiary(int beneficiariesId, String token) {
        String ownerUserName = tokenManager.parseToken(token);
        Client client = clientRepo.findByUserName(ownerUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        Beneficiary beneficiary = beneficiaryRepo.findById(beneficiariesId).orElseThrow(
                () -> new BeneficiaryNotFound(ErrorMessageConstants.BENEFICIARY_NOT_FOUND_EXCEPTION_MESSAGE));
        if (!client.getBeneficiaries().contains(beneficiary)) {
            throw new BeneficiaryNotFound(ErrorMessageConstants.BENEFICIARY_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        client.getBeneficiaries().remove(beneficiary);
        beneficiary.setBeneficiaryOwner(null);
        beneficiaryRepo.deleteById(beneficiariesId);

        return GeneralMessageConstants.SUCCESSFUL_BENEFICIARY_DELETION_MESSAGE;
    }

}
