package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT token pair returned after authentication.")
public record TokenDTO(
        @Schema(description = "JWT access token.", example = "eyJhbGciOiJIUzI1NiJ9.access-token")
        String accessToken,

        @Schema(description = "Refresh token used to renew access.", example = "eyJhbGciOiJIUzI1NiJ9.refresh-token")
        String refreshToken,

        @Schema(description = "Token type for the Authorization header.", example = "Bearer")
        String tokenType,

        @Schema(description = "Access token lifetime in seconds.", example = "900")
        long expiresIn
) {
}
