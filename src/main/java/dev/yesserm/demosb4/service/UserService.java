package dev.yesserm.demosb4.service;

import dev.yesserm.demosb4.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto dto);
    List<UserDto> findAll();
    UserDto findById(Long id);
}
