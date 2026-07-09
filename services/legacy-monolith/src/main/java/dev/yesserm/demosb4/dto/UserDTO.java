package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Set;

@Schema(description = "User profile and audit metadata.")
public record UserDTO(
        @Schema(description = "Internal user identifier.", example = "1")
        Long id,

        @Schema(description = "Full display name.", example = "Ana Martinez")
        String name,

        @Schema(description = "Unique email address.", example = "ana.martinez@example.com")
        String email,

        @Schema(description = "Optional phone number.", example = "+50588887777")
        String phone,

        @Schema(description = "Optional avatar URL.", example = "https://cdn.example.com/avatars/ana.png")
        String avatar,

        @Schema(description = "Whether the user account is active.", example = "true")
        boolean active,

        @Schema(description = "Assigned role names.", example = "[\"USER\"]")
        Set<String> roles,

        @Schema(description = "Creation timestamp.", example = "2026-07-08T21:00:00Z")
        Instant createdAt,

        @Schema(description = "Last update timestamp.", example = "2026-07-08T21:30:00Z")
        Instant updatedAt,

        @Schema(description = "Principal that created the record.", example = "system")
        String createdBy,

        @Schema(description = "Principal that last updated the record.", example = "admin@example.com")
        String updatedBy
) {
}
