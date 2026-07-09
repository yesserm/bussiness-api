package dev.yesserm.demosb4.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank @Size(max = 100) String name,
        @Email @NotBlank @Size(max = 150) String email,
        @Size(max = 30) String phone,
        @Size(max = 500) String avatar
) {
}
