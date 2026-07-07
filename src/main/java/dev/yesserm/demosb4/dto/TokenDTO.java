package dev.yesserm.demosb4.dto;

public record TokenDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
}
