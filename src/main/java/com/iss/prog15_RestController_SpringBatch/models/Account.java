package com.iss.prog15_RestController_SpringBatch.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tblaccount")
public class Account {
    @Id
    private Integer accountId;
    private  String name;
    private String branch;
    private String type;
    private double amount;
}
