package dev.yesserm.demosb4.authservice.dto;

public record TokenDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
}
