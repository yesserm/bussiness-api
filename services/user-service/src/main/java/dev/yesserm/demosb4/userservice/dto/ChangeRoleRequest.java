package dev.yesserm.demosb4.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeRoleRequest(
        @NotBlank @Size(max = 50) String role
) {
}
