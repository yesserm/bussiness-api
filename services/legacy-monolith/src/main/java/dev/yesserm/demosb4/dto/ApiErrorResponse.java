package dev.yesserm.demosb4.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiErrorResponse", description = "RFC 9457 problem detail response returned by the global exception handler.")
public record ApiErrorResponse(
        @Schema(example = "https://tools.ietf.org/html/rfc9110#section-15.5.1")
        String type,

        @Schema(example = "Bad Request")
        String title,

        @Schema(example = "400")
        int status,

        @Schema(example = "Request validation failed")
        String detail,

        @Schema(example = "/api/v1/users")
        String instance
) {
}
