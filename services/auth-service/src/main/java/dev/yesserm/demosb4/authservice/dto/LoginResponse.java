package dev.yesserm.demosb4.authservice.dto;

public record LoginResponse(
        TokenDTO tokens,
        UserDTO user
) {
}
