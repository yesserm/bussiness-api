package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used by an authenticated user to update their profile.")
public record UpdateProfileRequest(
        @Schema(description = "Full display name.", example = "Ana Martinez")
        @NotBlank @Size(max = 100) String name,

        @Schema(description = "Unique email address.", example = "ana.martinez@example.com")
        @Email @NotBlank @Size(max = 150) String email,

        @Schema(description = "Optional phone number.", example = "+50588887777")
        @Size(max = 30) String phone,

        @Schema(description = "Optional avatar URL.", example = "https://cdn.example.com/avatars/ana.png")
        @Size(max = 500) String avatar
) {
}
