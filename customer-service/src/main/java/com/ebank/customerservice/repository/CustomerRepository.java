package com.ebank.customerservice.repository;

import java.util.Optional;

import com.ebank.customerservice.entites.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

}