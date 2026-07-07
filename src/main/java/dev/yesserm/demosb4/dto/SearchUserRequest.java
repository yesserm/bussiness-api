package dev.yesserm.demosb4.dto;

import java.time.Instant;
import java.util.List;

public record SearchUserRequest(
        String name,
        String email,
        List<String> roles,
        Boolean active,
        Instant createdFrom,
        Instant createdTo
) {
}
