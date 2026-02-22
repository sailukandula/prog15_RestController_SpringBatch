package com.iss.prog15_RestController_SpringBatch.service;

import com.iss.prog15_RestController_SpringBatch.models.Account;

import java.util.List;

public interface IAccountServices {
    public List<Account> getAccounts();
    public void saveAccount(Account account);
    public void updateAccount(int accountId);
    public Account getAccountDetails(int accountId);
}
