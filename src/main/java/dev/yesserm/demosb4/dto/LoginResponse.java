package dev.yesserm.demosb4.dto;

public record LoginResponse(
        TokenDTO tokens,
        UserDTO user
) {
}
