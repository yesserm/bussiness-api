package dev.yesserm.demosb4.userservice.dto;

import java.time.Instant;
import java.util.Set;

public record UserDTO(
        Long id,
        String name,
        String email,
        String phone,
        String avatar,
        boolean active,
        Set<String> roles,
        Instant createdAt,
        Instant updatedAt,
        String createdBy,
        String updatedBy
) {
}
