package dev.yesserm.demosb4.userservice.service.impl;

import dev.yesserm.demosb4.userservice.dto.UserDTO;
import dev.yesserm.demosb4.userservice.exception.UserNotFoundException;
import dev.yesserm.demosb4.userservice.repository.UserRepository;
import dev.yesserm.demosb4.userservice.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDTO findById(Long id) {
        return repository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserDTO getProfile(Authentication authentication) {
        return repository.findByEmail(authentication.getName())
                .map(UserMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(authentication.getName()));
    }
}
