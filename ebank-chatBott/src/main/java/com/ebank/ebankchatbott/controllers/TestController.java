package com.ebank.ebankchatbott.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-public")
    public String test() {
        return "Ebank ChatBott fonctionne !";
    }
    @GetMapping("/test-jwt")
    public String testJwt(
            @org.springframework.security.core.annotation.AuthenticationPrincipal
            org.springframework.security.oauth2.jwt.Jwt jwt) {

        if (jwt == null) {
            return "JWT absent";
        }

        return "JWT reçu : " + jwt.getSubject()
                + " | Issuer : " + jwt.getIssuer();
    }

    @GetMapping("/test-jwt-details")
    public String testJwtDetails(
            org.springframework.security.core.Authentication authentication) {

        if (authentication == null) {
            return "Aucune authentification";
        }

        return "Authenticated: " + authentication.isAuthenticated()
                + " | Principal: " + authentication.getPrincipal()
                + " | Authorities: " + authentication.getAuthorities();
    }

}