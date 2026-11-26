
package com.ebank.authservice.config;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;

@Configuration
public class JwtConfig {

    @Bean
    public RSAKey rsaKey() throws Exception {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (InputStream inputStream = getClass()
                .getResourceAsStream("/keys/ebank-jwt.p12")) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Fichier ebank-jwt.p12 introuvable"
                );
            }

            keyStore.load(inputStream, "changeit".toCharArray());
        }

        RSAPrivateKey privateKey =
                (RSAPrivateKey) keyStore.getKey(
                        "ebank-jwt",
                        "changeit".toCharArray()
                );

        RSAPublicKey publicKey =
                (RSAPublicKey) keyStore.getCertificate("ebank-jwt")
                        .getPublicKey();

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID("ebank-jwt-key")
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAKey rsaKey) {

        ImmutableJWKSet<SecurityContext> jwkSource =
                new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }
    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws Exception {

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .signatureAlgorithm(SignatureAlgorithm.RS256)
                .build();

        decoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer("ebank-auth-service")
        );

        return decoder;
    }

}
