package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Advanced user search filters.")
public record SearchUserRequest(
        @Schema(description = "Name fragment.", example = "Ana")
        String name,

        @Schema(description = "Email fragment.", example = "example.com")
        String email,

        @Schema(description = "Role names to include.", example = "[\"USER\", \"ADMIN\"]")
        List<String> roles,

        @Schema(description = "Filter by active status.", example = "true")
        Boolean active,

        @Schema(description = "Inclusive creation date lower bound.", example = "2026-01-01T00:00:00Z")
        Instant createdFrom,

        @Schema(description = "Inclusive creation date upper bound.", example = "2026-12-31T23:59:59Z")
        Instant createdTo
) {
}
