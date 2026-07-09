package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credentials used to authenticate an existing user.")
public record LoginRequest(
        @Schema(description = "User email address.", example = "admin@example.com")
        @Email @NotBlank String email,

        @Schema(description = "User password.", example = "StrongPassword123")
        @NotBlank String password
) {
}
