
package com.ebank.authservice.controllers;

import java.util.Map;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwkSetController {

    private final RSAKey rsaKey;

    public JwkSetController(RSAKey rsaKey) {
        this.rsaKey = rsaKey;
    }

    @GetMapping(
            value = "/oauth2/jwks",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> jwks() {

        RSAKey publicKey = this.rsaKey.toPublicJWK();

        return new JWKSet(publicKey).toJSONObject();
    }
}
