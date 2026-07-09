package dev.yesserm.demosb4.userservice.service;

import dev.yesserm.demosb4.userservice.dto.UserDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO getProfile(Authentication authentication);
}
