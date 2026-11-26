
package com.ebank.authservice.controllers;

import com.ebank.authservice.dto.LoginRequest;
import com.ebank.authservice.dto.LoginResponse;
import com.ebank.authservice.dto.RegisterRequest;
import com.ebank.authservice.dto.UserResponse;
import com.ebank.authservice.entities.Role;
import com.ebank.authservice.entities.User;
import com.ebank.authservice.security.JwtService;
import com.ebank.authservice.services.UserService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {

        User user = this.userService.register(
                request.username(),
                request.email(),
                request.password(),
                Role.CUSTOMER
        );

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        User user = this.userService.login(
                request.email(),
                request.password()
        );

        String token = this.jwtService.generateToken(user);

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createEmployee(
            @Valid @RequestBody RegisterRequest request) {

        User user = this.userService.register(
                request.username(),
                request.email(),
                request.password(),
                Role.EMPLOYEE
        );

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
