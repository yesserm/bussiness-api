package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used by an administrator to assign a role to a user.")
public record ChangeRoleRequest(
        @Schema(description = "Role name without the ROLE_ prefix.", example = "ADMIN")
        @NotBlank @Size(max = 50) String role
) {
}
