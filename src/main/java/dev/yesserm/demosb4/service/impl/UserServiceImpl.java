package dev.yesserm.demosb4.service.impl;

import dev.yesserm.demosb4.dto.UserDto;
import dev.yesserm.demosb4.model.User;
import dev.yesserm.demosb4.repository.UserRepository;
import dev.yesserm.demosb4.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo){
        this.repo = repo;
    }

    @Override
    public UserDto create(UserDto dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        User saved = repo.save(user);

        return new UserDto(saved.getId(), saved.getName(), saved.getEmail());
    }

    @Override
    public List<UserDto> findAll() {
        return repo.findAll()
                .stream()
                .map(u -> new UserDto(u.getId(), u.getName(), u.getEmail()))
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        return repo.findById(id)
                .map(u -> new UserDto(u.getId(), u.getName(), u.getEmail()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
