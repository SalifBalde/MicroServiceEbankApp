package com.ebank.customerservice.repository;

import com.ebank.customerservice.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository  extends JpaRepository<Customer,Long > {
}
