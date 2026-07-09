package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication result with tokens and authenticated user profile.")
public record LoginResponse(
        @Schema(description = "Issued JWT tokens.")
        TokenDTO tokens,

        @Schema(description = "Authenticated user profile.")
        UserDTO user
) {
}
