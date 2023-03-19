// package com.base.BaseDependencies.Service;

// import java.util.List;
// import java.util.Optional;

// import org.modelmapper.ModelMapper;
// import org.springframework.stereotype.Service;

// import com.base.BaseDependencies.Dtos.AccountDto;
// import com.base.BaseDependencies.Models.Account;
// import com.base.BaseDependencies.Models.Client;
// import com.base.BaseDependencies.Repository.AccountRepo;
// import com.base.BaseDependencies.Repository.ClientRepo;
// import com.base.BaseDependencies.Utils.AccountNumberGenerator;
// import com.base.BaseDependencies.Utils.JwtManager;

// import lombok.AllArgsConstructor;


// @AllArgsConstructor
// @Service
// public class AccountService {
//     private AccountRepo accountRepo;
//     private ClientRepo clientRepo;
//     private JwtManager tokenManager;
//     private AccountNumberGenerator accountNumberGenerator;
//     private ModelMapper modelMapper;

//     public Account createAccount(AccountDto newAccountDto, String token) throws Exception {
//         String ownerDetails = tokenManager.parseToken(token);
//         Optional<Client> getClient = clientRepo.findById(ownerDetails);
//         if (getClient.isPresent()) {
//             Account newAccount = modelMapper.map(newAccountDto, Account.class);
//             newAccount.setAccountNumber(accountNumberGenerator.generateAccountNumber(12));
//             newAccount.setOwnerId(getClient.get());
//             return accountRepo.save(newAccount);
//         }
//         throw new Exception("No Client Found");
//     }

//     public Boolean deleteAccount(Long accountId, String token) throws Exception {
//         String ownerDetails = tokenManager.parseToken(token);
//         Optional<Client> getClient = clientRepo.findById(ownerDetails);
//         Optional<Account> getAccount = accountRepo.findById(accountId);
//         if (getClient.isEmpty()) {
//             throw new Exception("Client Not Found");
//         } else if (getAccount.isEmpty()) {
//             throw new Exception("Account Not Found");
//         }
//         accountRepo.deleteByAccountNumberAndOwnerId(accountId, ownerDetails);
//         return true;

//     }

//     public boolean updateAccountBalance(Account account, String token) throws Exception {
//         String ownerDetails = tokenManager.parseToken(token);
//         Optional<Client> getClient = clientRepo.findById(ownerDetails);
//         if (getClient.isPresent()) {
//             accountRepo.save(account);
//             return true;
//         }
//         throw new Exception("Client Not Found");
//     }

//     public Optional<List<Account>> getAccountByClientId(String token) throws Exception {
//         String ownerDetails = tokenManager.parseToken(token);
//         Optional<Client> getClient = clientRepo.findById(ownerDetails);
//         if (getClient.isPresent()) {
//             return accountRepo.findByOwnerId(ownerDetails);
//         }
//         throw new Exception("No Accounts Available");
//     }

//     public Account getAccountById(long accountNumber, String token) throws Exception {
//         String ownerDetails = tokenManager.parseToken(token);
//         Optional<Client> getClient = clientRepo.findById(ownerDetails);
//         Optional<Account> getAccount = accountRepo.findById(accountNumber);
//         if (getClient.isEmpty()) {
//             throw new Exception("Client Not Found");
//         } else if (getAccount.isEmpty()) {
//             throw new Exception("Account Not Found");
//         }
//             return getAccount.get();
//     }

//     public List<Account> getAllAccounts() {
//         return accountRepo.findAll();
//     }
// }
