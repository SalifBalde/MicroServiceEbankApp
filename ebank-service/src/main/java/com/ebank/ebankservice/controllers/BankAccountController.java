
package com.ebank.ebankservice.controllers;

import java.util.List;

import com.ebank.ebankservice.entities.BankAccount;
import com.ebank.ebankservice.services.BankAccountService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    // Vérifier le rôle depuis les claims du JWT
    private boolean hasRole(Jwt jwt, String role) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles != null && roles.contains(role);
    }

    // Consulter les comptes bancaires
    @GetMapping("/accounts")
    public List<BankAccount> allAccount(
            @AuthenticationPrincipal Jwt jwt) {

        log.info("JWT SUBJECT = {}", jwt.getSubject());
        log.info("JWT ROLES = {}", jwt.getClaimAsStringList("roles"));

        if (hasRole(jwt, "ADMIN") || hasRole(jwt, "EMPLOYEE")) {
            return bankAccountService.allAccount();
        }

        if (hasRole(jwt, "CUSTOMER")) {
            return bankAccountService.allAccount(jwt.getSubject());
        }

        throw new AccessDeniedException("Accès refusé");
    }

    // Consulter un compte par ID
    @GetMapping("/accounts/{id}")
    public BankAccount findAccountById(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {

        if (hasRole(jwt, "ADMIN") || hasRole(jwt, "EMPLOYEE")) {
            return bankAccountService.findAccountById(id);
        }

        if (hasRole(jwt, "CUSTOMER")) {
            return bankAccountService.findAccountByIdForUser(
                    id, jwt.getSubject());
        }

        throw new AccessDeniedException("Accès refusé");
    }

    // Créer un compte
    @PostMapping("/account")
    public BankAccount saveAccount(
            @RequestBody BankAccount bankAccount,
            @AuthenticationPrincipal Jwt jwt) {

        if (hasRole(jwt, "ADMIN") || hasRole(jwt, "EMPLOYEE")) {
            return bankAccountService.saveAccount(bankAccount);
        }

        if (hasRole(jwt, "CUSTOMER")) {
            return bankAccountService.saveAccountForUser(
                    bankAccount, jwt.getSubject());
        }

        throw new AccessDeniedException("Accès refusé");
    }

    // Modifier un compte
    @PutMapping("/account/{id}")
    public BankAccount updateAccount(
            @PathVariable String id,
            @RequestBody BankAccount bankAccount,
            @AuthenticationPrincipal Jwt jwt) {

        if (hasRole(jwt, "ADMIN") || hasRole(jwt, "EMPLOYEE")) {
            return bankAccountService.updateAccount(id, bankAccount);
        }

        if (hasRole(jwt, "CUSTOMER")) {
            return bankAccountService.updateAccountForUser(
                    id, bankAccount, jwt.getSubject());
        }

        throw new AccessDeniedException("Accès refusé");
    }

    // Supprimer un compte
    @DeleteMapping("/account/{id}")
    public void deleteAccount(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {

        if (hasRole(jwt, "ADMIN")) {
            bankAccountService.deleteAccount(id);
            return;
        }

        if (hasRole(jwt, "CUSTOMER")) {
            bankAccountService.deleteAccountForUser(
                    id, jwt.getSubject());
            return;
        }

        throw new AccessDeniedException("Accès refusé");
    }
}
