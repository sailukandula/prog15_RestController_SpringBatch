package com.iss.prog15_RestController_SpringBatch.service;

import com.iss.prog15_RestController_SpringBatch.models.Account;
import com.iss.prog15_RestController_SpringBatch.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AccountServices implements IAccountServices {
    @Autowired
    IAccountRepository accountRepository;

    @Override
    public List<Account> getAccounts() {
       return accountRepository.findAll();
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);

    }

    @Override
    public void updateAccount(int accountId) {

    }

    @Override
    public Account getAccountDetails(int accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }
}
