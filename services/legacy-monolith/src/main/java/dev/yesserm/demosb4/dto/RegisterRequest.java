package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used to register a new user.")
public record RegisterRequest(
        @Schema(description = "Full display name.", example = "Ana Martinez")
        @NotBlank @Size(max = 100) String name,

        @Schema(description = "Unique email address.", example = "ana.martinez@example.com")
        @Email @NotBlank @Size(max = 150) String email,

        @Schema(description = "Password with at least eight characters.", example = "StrongPassword123")
        @NotBlank @Size(min = 8, max = 100) String password
) {
}
