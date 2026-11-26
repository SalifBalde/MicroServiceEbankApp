package com.ebank.authservice;

import com.ebank.authservice.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    /**
     * @param userService
     * @return
     */
    @Bean
    CommandLineRunner resetAdminPassword(
            UserService userService) {

        return args -> {
            userService.ensureAdminAccount("Balde123");
            System.out.println("Mot de passe ADMIN réinitialisé !");
        };
    }
}
