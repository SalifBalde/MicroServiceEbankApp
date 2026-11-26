
package com.ebank.authservice.services;

import com.ebank.authservice.entities.Role;
import com.ebank.authservice.entities.User;
import com.ebank.authservice.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(
            String username,
            String email,
            String password,
            Role role) {

        if (this.userRepository.existsByEmail(email)) {
            throw new RuntimeException("Cet email existe déjà");
        }

        if (this.userRepository.existsByUsername(username)) {
            throw new RuntimeException("Ce nom d'utilisateur existe déjà");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(this.passwordEncoder.encode(password))
                .role(role)
                .enabled(true)
                .build();

        return this.userRepository.save(user);
    }

    public User login(String email, String rawPassword) {

        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Email ou mot de passe incorrect"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Compte désactivé");
        }

        if (!this.passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        return user;
    }
    public void ensureAdminAccount(String newPassword) {

        if (userRepository.findByEmail("admin@ebank.com").isEmpty()) {

            register(
                    "admin",
                    "admin@ebank.com",
                    newPassword,
                    Role.ADMIN
            );

            System.out.println("Compte ADMIN créé !");
        }
    }
}
