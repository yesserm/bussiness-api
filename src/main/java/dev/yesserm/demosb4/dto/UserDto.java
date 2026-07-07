package dev.yesserm.demosb4.dto;

import java.util.Set;

public record UserDTO(
        Long id,
        String name,
        String email,
        Set<String> roles
) {
}
