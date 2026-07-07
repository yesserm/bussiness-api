package dev.yesserm.demosb4.service.impl;

import dev.yesserm.demosb4.dto.UserDTO;
import dev.yesserm.demosb4.model.User;
import dev.yesserm.demosb4.repository.UserRepository;
import dev.yesserm.demosb4.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getProfile(Authentication authentication) {
        return repository.findByEmail(authentication.getName())
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserDTO toDto(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        return new UserDTO(user.getId(), user.getName(), user.getEmail(), roles);
    }
}
