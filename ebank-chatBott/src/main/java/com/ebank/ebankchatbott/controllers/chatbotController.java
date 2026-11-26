
package com.ebank.ebankchatbott.controllers;

import com.ebank.ebankchatbott.agents.EbankAgent;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ChatbotController {

    private final EbankAgent ebankAgent;

    public ChatbotController(EbankAgent ebankAgent) {
        this.ebankAgent = ebankAgent;
    }

    @GetMapping("/chat")
    public String chat(
            @RequestParam(name = "query", defaultValue = "Bonjour")
            String query,

            @RequestParam
            String conversationId,

            @AuthenticationPrincipal Jwt jwt) {

        if (jwt == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentification requise"
            );
        }

        String accessToken = jwt.getTokenValue();

        return ebankAgent.chat(
                query,
                conversationId,
                accessToken
        );
    }
}
