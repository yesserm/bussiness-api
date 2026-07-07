package dev.yesserm.demosb4.controller;

import dev.yesserm.demosb4.dto.UserDto;
import dev.yesserm.demosb4.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto dto){
        return service.create(dto);
    }

    @GetMapping
    public List<UserDto> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id){
        return service.findById(id);
    }
}
