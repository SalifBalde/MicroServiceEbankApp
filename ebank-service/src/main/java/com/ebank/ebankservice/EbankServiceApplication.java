
package com.ebank.ebankservice;

import java.util.Date;
import java.util.UUID;

import com.ebank.ebankservice.entities.BankAccount;
import com.ebank.ebankservice.repository.BankAccountRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            BankAccountRepository bankAccountRepository) {

        return args -> {
            for (int i = 1; i <= 3; i++) {
                for (int j = 0; j < 5; j++) {

                    BankAccount account = BankAccount.builder()
                            .id(UUID.randomUUID().toString())
                            .type(Math.random() > 0.5
                                    ? "CURRENT-ACCOUNT"
                                    : "SAVINGS-ACCOUNT")
                            .balance(1000 + Math.random() * 60000)
                            .customerId(i)
                            .createAt(new Date())
                            .build();

                    bankAccountRepository.save(account);
                }
            }

            System.out.println(
                    "Initialisation des comptes bancaires terminée."
            );
        };
    }
}
