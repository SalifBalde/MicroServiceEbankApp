
package com.ebank.ebankservice.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.ebank.ebankservice.entities.BankAccount;
import com.ebank.ebankservice.exceptions.AccountNotFoundException;
import com.ebank.ebankservice.feign.CustomerRestClient;
import com.ebank.ebankservice.models.Customer;
import com.ebank.ebankservice.repository.BankAccountRepository;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRestClient customerRestClient;

    public BankAccountService(
            BankAccountRepository bankAccountRepository,
            CustomerRestClient customerRestClient) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRestClient = customerRestClient;
    }

    // ==========================================
    // OUTILS MCP
    // ==========================================

    // ADMIN et EMPLOYEE : consulter tous les comptes
    @McpTool(description = "get All Bank Accounts")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public List<BankAccount> allAccount() {
        List<BankAccount> accounts = this.bankAccountRepository.findAll();

        accounts.forEach(account ->
                account.setCustomer(
                        this.customerRestClient.getCustomer(account.getCustomerId())
                )
        );

        return accounts;
    }

    // ADMIN et EMPLOYEE : consulter un compte par ID
    @McpTool(description = "get Bank Account by id")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public BankAccount findAccountById(
            @McpToolParam(description = "the bank Account id") String id) {

        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Bank account not found with id: " + id
                ));

        bankAccount.setCustomer(
                customerRestClient.getCustomer(bankAccount.getCustomerId())
        );

        return bankAccount;
    }

    // ADMIN et EMPLOYEE : créer un compte
    @McpTool(description = "save new bank account")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public BankAccount saveAccount(
            @McpToolParam(description = "the bank account to save (balance, type, customerId)")
            BankAccount bankAccount) {

        customerRestClient.getCustomer(bankAccount.getCustomerId());

        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreateAt(new Date());

        return bankAccountRepository.save(bankAccount);
    }

    // ADMIN et EMPLOYEE : modifier un compte
    @McpTool(description = "Update an existing bank account")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public BankAccount updateAccount(
            @McpToolParam(description = "the bank account id") String id,
            @McpToolParam(description = "the updated bank account data")
            BankAccount updatedAccount) {

        BankAccount existingAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Bank account not found with id: " + id
                ));

        customerRestClient.getCustomer(updatedAccount.getCustomerId());

        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setType(updatedAccount.getType());
        existingAccount.setCustomerId(updatedAccount.getCustomerId());

        return bankAccountRepository.save(existingAccount);
    }

    // ADMIN uniquement : supprimer un compte
    @McpTool(description = "Delete a bank account by id")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccount(
            @McpToolParam(description = "the bank account id") String id) {

        BankAccount existingAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Bank account not found with id: " + id
                ));

        bankAccountRepository.delete(existingAccount);
    }

    // ==========================================
    // MÉTHODES POUR LES CLIENTS CONNECTÉS
    // ==========================================

    // Récupérer uniquement les comptes du client connecté
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'EMPLOYEE')")
    public List<BankAccount> allAccount(String email) {

        Customer customer = customerRestClient.getCustomerByEmail(email);

        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Client introuvable");
        }

        List<BankAccount> accounts = bankAccountRepository
                .findByCustomerId(customer.getId());

        accounts.forEach(account -> account.setCustomer(customer));

        return accounts;
    }

    // Consulter un compte uniquement si l'utilisateur en est propriétaire
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'EMPLOYEE')")
    public BankAccount findAccountByIdForUser(String id, String email) {

        Customer customer = customerRestClient.getCustomerByEmail(email);

        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Client introuvable");
        }

        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Bank account not found with id: " + id
                ));

        if (!Objects.equals(bankAccount.getCustomerId(), customer.getId())) {
            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à consulter ce compte bancaire"
            );
        }

        bankAccount.setCustomer(customer);

        return bankAccount;
    }

    // Modifier un compte uniquement si l'utilisateur en est propriétaire
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'EMPLOYEE')")
    public BankAccount updateAccountForUser(
            String id,
            BankAccount updatedAccount,
            String email) {

        Customer customer = customerRestClient.getCustomerByEmail(email);

        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Client introuvable");
        }

        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Bank account not found with id: " + id
                ));

        if (!Objects.equals(account.getCustomerId(), customer.getId())) {
            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à modifier ce compte bancaire"
            );
        }

        account.setBalance(updatedAccount.getBalance());
        account.setType(updatedAccount.getType());

        return bankAccountRepository.save(account);
    }

    // Supprimer un compte uniquement si l'utilisateur en est propriétaire
    @PreAuthorize("hasRole('CUSTOMER')")
    public void deleteAccountForUser(String id, String email) {

        Customer customer = customerRestClient.getCustomerByEmail(email);

        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Client introuvable");
        }

        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Bank account not found with id: " + id
                ));

        if (!Objects.equals(account.getCustomerId(), customer.getId())) {
            throw new AccessDeniedException(
                    "Vous n'êtes pas autorisé à supprimer ce compte bancaire"
            );
        }

        bankAccountRepository.delete(account);
    }

    // Créer un compte pour le client connecté
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'EMPLOYEE')")
    public BankAccount saveAccountForUser(
            BankAccount bankAccount,
            String email) {

        Customer customer = customerRestClient.getCustomerByEmail(email);

        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Client introuvable");
        }

        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreateAt(new Date());
        bankAccount.setCustomerId(customer.getId());

        return bankAccountRepository.save(bankAccount);
    }


    @McpTool(description = "Get only the bank accounts of the authenticated customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<BankAccount> myAccounts() {

        // 1. Récupérer l'identité depuis le JWT authentifié
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Utilisateur non authentifié");
        }

        // Le subject du JWT contient l'email
        String email = authentication.getName();

        // 2. Retrouver le client dans customer-service
        Customer customer = customerRestClient.getCustomerByEmail(email);

        if (customer == null || customer.getId() == null) {
            throw new RuntimeException("Client introuvable");
        }

        // 3. Récupérer uniquement les comptes de ce client
        List<BankAccount> accounts =
                bankAccountRepository.findByCustomerId(customer.getId());

        // 4. Associer les informations du client aux comptes
        accounts.forEach(account -> account.setCustomer(customer));

        return accounts;

    }

}
