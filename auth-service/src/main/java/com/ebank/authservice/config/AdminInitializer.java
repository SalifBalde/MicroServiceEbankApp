
package com.ebank.authservice.config;

import com.ebank.authservice.entities.Role;
import com.ebank.authservice.entities.User;
import com.ebank.authservice.repositories.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (!userRepository.existsByEmail("admin@ebank.com")) {

                User admin = User.builder()
                        .username("admin")
                        .email("admin@ebank.com")
                        .password(passwordEncoder.encode("Admin@2026"))
                        .role(Role.ADMIN)
                        .enabled(true)
                        .build();

                userRepository.save(admin);

                System.out.println("Compte ADMIN créé avec succès !");
            }
        };
    }
}
