package com.iss.prog15_RestController_SpringBatch.controller;

import com.iss.prog15_RestController_SpringBatch.models.Account;
import com.iss.prog15_RestController_SpringBatch.service.IAccountServices;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("csvToBrowser")
    private Job csvJob;

    @Autowired
    @Qualifier("dbFromCsvJob")
    private  Job dbJob;

    @Autowired
    private List<Account> sharedResultList;

    @Autowired
    IAccountServices accountServices;

    @GetMapping("/getAccounts")
    @ResponseBody
    public List<Account> getAccounts(){
        return accountServices.getAccounts();
    }

    @GetMapping("/getAccountDetails")
    @ResponseBody
    public Account getAccountDetails(@RequestParam int accountId){
        return accountServices.getAccountDetails(accountId);
    }

    @GetMapping("/listCsvRecords")
    @ResponseBody
    public List<Account> listCsvRecords() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("start",System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(csvJob,jobParameters).getExitStatus();
        return sharedResultList;
    }


    @GetMapping("/getBalance")
    @ResponseBody
    public double getAmount(){
      return sharedResultList.stream()
                .mapToDouble(Account::getAmount)
                .sum();
    }

    @GetMapping("/saveCsvToDB")
    @ResponseBody
    public String saveCsvToDB() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("start",System.currentTimeMillis())
                .toJobParameters();
       var exitStatus = jobLauncher.run(dbJob,jobParameters).getExitStatus();

       return  exitStatus.getExitCode();

    }
    @PostMapping("/addAccount")
    @ResponseBody
    public ResponseEntity<Account> addAccount(Account account){
      accountServices.saveAccount(account);
      return ResponseEntity.ok(account);
    }
}
