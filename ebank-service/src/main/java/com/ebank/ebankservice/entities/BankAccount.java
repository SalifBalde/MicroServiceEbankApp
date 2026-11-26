package com.ebank.ebankservice.entities;

import com.ebank.ebankservice.models.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.nio.file.FileStore;
import java.util.Date;


@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @ToString
@Builder
public class BankAccount {

   @Id
    private String id ;
 @Temporal(TemporalType.TIMESTAMP)
 private Date createAt;
    private  double balance;
    private  String type;
    private long  customerId;

    @Transient
    private Customer customer;


}
