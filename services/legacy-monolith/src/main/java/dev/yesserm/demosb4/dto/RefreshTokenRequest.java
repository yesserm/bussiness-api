package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload used to renew an access token.")
public record RefreshTokenRequest(
        @Schema(description = "Valid refresh token issued by the authentication service.", example = "eyJhbGciOiJIUzI1NiJ9.refresh-token")
        @NotBlank String refreshToken
) {
}
