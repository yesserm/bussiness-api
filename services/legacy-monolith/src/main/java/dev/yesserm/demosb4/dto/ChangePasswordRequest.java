package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used by an authenticated user to change their password.")
public record ChangePasswordRequest(
        @Schema(description = "Current password.", example = "CurrentPassword123")
        @NotBlank String currentPassword,

        @Schema(description = "New password with at least eight characters.", example = "NewStrongPassword123")
        @NotBlank @Size(min = 8, max = 100) String newPassword
) {
}
