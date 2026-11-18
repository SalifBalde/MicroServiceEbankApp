package com.ebank.ebankservice.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ebank.ebankservice.entities.BankAccount;
import com.ebank.ebankservice.feign.CustomerRestClient;
import com.ebank.ebankservice.models.Customer;
import com.ebank.ebankservice.repository.BankAccountRepository;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;


@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final  CustomerRestClient customerRestClient;

    public BankAccountService(BankAccountRepository bankAccountRepository, CustomerRestClient customerRestClient) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRestClient = customerRestClient;
    }

    @McpTool(description = "get All Bank Accounts")
    public List<BankAccount> allAccount(){
        return this.bankAccountRepository.findAll();
    }



    @McpTool(description = "get  Bank Account by id")
    public BankAccount findAccountById(@McpToolParam(description = "the bank Account id") String id){
        BankAccount bankAccount = this.bankAccountRepository.findById(id).orElseThrow(() -> new RuntimeException(" Account not found"));
        bankAccount.setCustomer(customerRestClient.getCustomer(bankAccount.getCustomerId()));
        return  bankAccount;
    }

    @McpTool(description = "save new bank account ")
    public  BankAccount saveAccount(@McpToolParam(description = "the bank account to save (balance, type,customerId)") BankAccount bankAccount) throws RuntimeException {
        try {
            Customer customer = this.customerRestClient.getCustomer(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreateAt(new Date());
            return this.bankAccountRepository.save(bankAccount);

        } catch (Exception e) {
             throw  new RuntimeException(e.getMessage());
        }

    }

}
