package com.iss.prog15_RestController_SpringBatch.configuration;

import com.iss.prog15_RestController_SpringBatch.models.Account;
import com.iss.prog15_RestController_SpringBatch.repository.IAccountRepository;
import com.iss.prog15_RestController_SpringBatch.service.IAccountServices;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AccountConfig {

    @Autowired
    IAccountRepository accountRepository;

    @Bean
    public List<Account> sharedResultList(){
        return new ArrayList<>();
    }
    //Flat file writer to Browser using below  code
    @Bean
    public FlatFileItemReader<Account> csvReader(){
        return new FlatFileItemReaderBuilder<Account>()
                .name("useReader")
                .resource(new ClassPathResource("accounts.csv"))
                .delimited()
                .names("accountId","name","type","branch","amount")
                .linesToSkip(1)
                .targetType(Account.class)
                .build();
    }

    @Bean
    public ItemWriter<Account> itemWriter(List<Account> sharedResultsList){
        return new ItemWriter<Account>() {
            @Override
            public void write(Chunk<? extends Account> chunk) throws Exception {
                sharedResultsList.addAll(chunk.getItems());
            }
        };
    }

    @Bean
    public Step csvStep (JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                         FlatFileItemReader<Account> csvReader, ItemWriter<Account> itemWriter){
        return new StepBuilder("csvStep", jobRepository)
                .<Account, Account>chunk(10, platformTransactionManager)
                .reader(csvReader)
                .writer(itemWriter)
                .build();
    }
    @Bean
    public Job csvToBrowser(JobRepository jobRepository, Step csvStep){
        return new JobBuilder("csvJob",jobRepository)
                .start(csvStep)
                .build();
    }
// Writing into database
@Bean
public FlatFileItemReader<Account> dbReader(){
    return new FlatFileItemReaderBuilder<Account>()
            .name("dbReader")
            .resource(new ClassPathResource("accounts.csv"))
            .delimited()
            .names("accountId","name","type","branch","amount")
            .linesToSkip(1)
            .targetType(Account.class)
            .build();
}
    @Bean
    public ItemWriter<Account> itemDBWriter(){
       return chunk -> accountRepository.saveAll(chunk.getItems());
    }

    @Bean
    public Step dbStep (JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                         FlatFileItemReader<Account> dbReader, ItemWriter<Account> itemDBWriter){
        return new StepBuilder("dbStep", jobRepository)
                .<Account, Account>chunk(10, platformTransactionManager)
                .reader(dbReader)
                .writer(itemDBWriter)
                .build();
    }
    @Bean
    public Job dbFromCsvJob(JobRepository jobRepository, Step dbStep){
        return new JobBuilder("dbJob",jobRepository)
                .start(dbStep)
                .build();

    }
}
