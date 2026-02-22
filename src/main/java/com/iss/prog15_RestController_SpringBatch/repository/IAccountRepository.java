package com.iss.prog15_RestController_SpringBatch.repository;

import com.iss.prog15_RestController_SpringBatch.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {

}
