package dev.yesserm.demosb4.service;

import dev.yesserm.demosb4.dto.UserDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO getProfile(Authentication authentication);
}
