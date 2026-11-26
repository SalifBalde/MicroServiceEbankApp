package com.ebank.customerservice.controllers;


import java.util.List;

import com.ebank.customerservice.entites.Customer;
import com.ebank.customerservice.services.CustomerService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/customer/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }



    @GetMapping("/customer/email/{email}")
    public Customer findCustomerByEmail(
            @PathVariable String email,
            @AuthenticationPrincipal Jwt jwt) {

        if (jwt.getClaimAsStringList("roles").contains("CUSTOMER")) {
            return customerService.findOwnProfile(email);
        }

        return customerService.findCustomerByEmail(email);
    }
}
