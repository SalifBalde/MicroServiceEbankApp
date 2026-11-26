
package com.ebank.ebankchatbott.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDiagnostic {

    @Bean
    public JwtDecoder jwtDecoder() {

        return NimbusJwtDecoder
                .withJwkSetUri(
                        "http://localhost:8085/oauth2/jwks"
                )
                .build();
    }
}
