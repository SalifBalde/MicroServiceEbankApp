
package com.ebank.authservice.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long userId,
        String username,
        String email,
        String role
) {}
