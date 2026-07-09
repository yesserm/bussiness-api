package dev.yesserm.demosb4.userservice.service.impl;

import dev.yesserm.demosb4.userservice.dto.UserDTO;
import dev.yesserm.demosb4.userservice.model.Role;
import dev.yesserm.demosb4.userservice.model.User;

import java.util.stream.Collectors;

final class UserMapper {
    private UserMapper() {
    }

    static UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatar(),
                user.isActive(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getCreatedBy(),
                user.getUpdatedBy()
        );
    }
}
