
package com.ebank.customerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        return http

                // CSRF : exclusion des routes H2 et MCP
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/h2-console/**",
                                "/mcp",
                                "/mcp/**"
                        )
                )

                // Console H2
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // API stateless avec JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Autorisations HTTP
                .authorizeHttpRequests(auth -> auth

                        // Console H2
                        .requestMatchers(
                                "/h2-console",
                                "/h2-console/**"
                        ).permitAll()

                        // Health check
                        .requestMatchers("/actuator/health").permitAll()

                        // MCP : accès HTTP public
                        // Les outils sensibles sont protégés par @PreAuthorize
                        .requestMatchers("/mcp", "/mcp/**").permitAll()

                        // Consultation des clients
                        .requestMatchers(HttpMethod.GET, "/customers/**")
                        .hasAnyRole("CUSTOMER", "EMPLOYEE", "ADMIN")

                        // Création d'un client
                        .requestMatchers(HttpMethod.POST, "/customers/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        // Enregistrement d'un client avec PATCH
                        .requestMatchers(HttpMethod.PATCH, "/customer")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        // Modification d'un client
                        .requestMatchers(HttpMethod.PUT, "/customers/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")

                        // Suppression d'un client
                        .requestMatchers(HttpMethod.DELETE, "/customer/**")
                        .hasRole("ADMIN")

                        // Tout le reste nécessite une authentification
                        .anyRequest().authenticated()
                )

                // Validation des JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                )

                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}
