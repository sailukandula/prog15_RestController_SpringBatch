package com.iss.prog15_RestController_SpringBatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Prog15RestControllerSpringBatchApp {

	public static void main(String[] args) {
		SpringApplication.run(Prog15RestControllerSpringBatchApp.class, args);
	}

}
