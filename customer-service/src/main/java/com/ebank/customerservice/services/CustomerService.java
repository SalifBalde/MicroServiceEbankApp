package com.ebank.customerservice.services;


import com.ebank.customerservice.entites.Customer;
import com.ebank.customerservice.repository.CustomerRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;


    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


   @McpTool(description = " get all customers")
    public List<Customer> allCustomer(){
        return  customerRepository.findAll();
    }

    @McpTool(description = "get customer by id")
    public Customer findCustomerById(@McpToolParam(description = "the customer id") Long id){
        return  customerRepository.findById(id).orElseThrow(() -> new RuntimeException(" Customer not found"));
    }

    @McpTool(description = "save new customer")
    public  Customer saveCustomer(@McpToolParam(description = "the customer to save (name, email)") Customer customer){
        return  customerRepository.save(customer);
    }



}
