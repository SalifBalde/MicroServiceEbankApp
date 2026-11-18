package com.ebank.customerservice.controllers;


import com.ebank.customerservice.entites.Customer;
import com.ebank.customerservice.services.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomersController {

    private CustomerService customerService;

    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> allCustomer(){
        return  customerService.allCustomer();
    }

    @GetMapping("/customer/{id}")
    public Customer findCustomerById(@PathVariable Long id){
        return  customerService.findCustomerById(id);
       }

       @PatchMapping("/customer")
    public  Customer saveCustomer(@RequestBody  Customer customer){
        return  customerService.saveCustomer(customer);
    }

}
