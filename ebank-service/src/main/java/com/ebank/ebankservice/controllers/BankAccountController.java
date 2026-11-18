package com.ebank.ebankservice.controllers;


import com.ebank.ebankservice.entities.BankAccount;
import com.ebank.ebankservice.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountController {

    private  final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts")
    public List<BankAccount> allAccount(){
        return  bankAccountService.allAccount();
    }

    @GetMapping("/accounts/{id}")
    public BankAccount findAccountById(@PathVariable String id){
        return  bankAccountService.findAccountById(id);
    }

    @PostMapping("/account")
    public  BankAccount saveAccount(@RequestBody BankAccount bankAccount){
        return  bankAccountService.saveAccount(bankAccount);
    }
}
