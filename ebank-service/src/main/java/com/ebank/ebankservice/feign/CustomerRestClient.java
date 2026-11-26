package com.ebank.ebankservice.feign;


import com.ebank.ebankservice.config.FeignAuthConfig;
import com.ebank.ebankservice.models.Customer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service",
        configuration = FeignAuthConfig.class
)
public interface CustomerRestClient {

    @GetMapping("/customer/{id}")
    @CircuitBreaker(name="customer-service", fallbackMethod = "getDefaultCustomer")
    Customer getCustomer(@PathVariable("id") Long id); // ✅ Spécifier explicitement le nom

   default Customer getDefaultCustomer( Long id, Exception e){
       return Customer.builder()
               .id(id)
               .name(" Not available")
               .email("Not available")
               .build();
   }

    @GetMapping("/customer/email/{email}")
    @CircuitBreaker(name = "customer-service", fallbackMethod = "getDefaultCustomerByEmail")
    Customer getCustomerByEmail(@PathVariable("email") String email);

    default Customer getDefaultCustomerByEmail(String email, Exception e) {

        System.err.println("ERREUR FEIGN CUSTOMER-SERVICE");
        System.err.println("Email demandé : " + email);
        e.printStackTrace();

        return Customer.builder()
                .name("Not available")
                .email("Not available")
                .build();
    }
}
