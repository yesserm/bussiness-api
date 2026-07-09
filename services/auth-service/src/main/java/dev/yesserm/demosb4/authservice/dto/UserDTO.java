package dev.yesserm.demosb4.authservice.dto;

import java.util.Set;

public record UserDTO(
        Long id,
        String name,
        String email,
        boolean active,
        Set<String> roles
) {
}
