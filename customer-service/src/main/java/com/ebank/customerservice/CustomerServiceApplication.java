
package com.ebank.customerservice;

import java.util.List;

import com.ebank.customerservice.entites.Customer;
import com.ebank.customerservice.repository.CustomerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            List<String> names = List.of("Salif", "Mohamed", "dev");

            names.forEach(name -> customerRepository.save(Customer.builder()
                    .name(name)
                    .email(name + "@gmail.com")
                    .build()));
        };
    }
}
