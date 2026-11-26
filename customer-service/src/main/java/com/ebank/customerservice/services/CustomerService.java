
package com.ebank.customerservice.services;

import java.util.List;

import com.ebank.customerservice.entites.Customer;
import com.ebank.customerservice.repository.CustomerRepository;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Réservé aux employés et administrateurs
    @McpTool(description = "Get all customers")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public List<Customer> allCustomer() {
        return customerRepository.findAll();
    }

    // Réservé aux employés et administrateurs
    @McpTool(description = "Get customer by id")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public Customer findCustomerById(
            @McpToolParam(description = "The customer id") Long id) {

        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));
    }

    // Réservé aux employés et administrateurs
    @McpTool(description = "Save new customer")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public Customer saveCustomer(
            @McpToolParam(description = "The customer to save (name, email)")
            Customer customer) {

        return customerRepository.save(customer);
    }

    // Réservé aux administrateurs
    @McpTool(description = "Delete a customer by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(
            @McpToolParam(description = "The ID of the customer to delete")
            Long id) {

        this.customerRepository.deleteById(id);
    }

    // Consultation du profil par ADMIN et EMPLOYEE
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));
    }


    // Consultation de son propre profil par CUSTOMER
    @PreAuthorize("hasRole('CUSTOMER') and #p0 == authentication.name")
    public Customer findOwnProfile(String email) {

        System.out.println("========== DEBUG FIND OWN PROFILE ==========");
        System.out.println("EMAIL RECU = [" + email + "]");
        System.out.println("EMAIL AUTHENTIFIE = [" +
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext().getAuthentication().getName() + "]");

        List<Customer> customers = customerRepository.findAll();
        System.out.println("NOMBRE DE CLIENTS = " + customers.size());

        customers.forEach(c ->
                System.out.println("CLIENT EN BASE = [" + c.getEmail() + "]"));

        return customerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));
    }


}
