package dev.yesserm.demosb4.userservice.controller;

import dev.yesserm.demosb4.contracts.pagination.PageResponse;
import dev.yesserm.demosb4.userservice.dto.ChangePasswordRequest;
import dev.yesserm.demosb4.userservice.dto.SearchUserRequest;
import dev.yesserm.demosb4.userservice.dto.UpdateProfileRequest;
import dev.yesserm.demosb4.userservice.dto.UserDTO;
import dev.yesserm.demosb4.userservice.service.UserManagementService;
import dev.yesserm.demosb4.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserManagementService userManagementService;

    public UserController(UserService userService, UserManagementService userManagementService) {
        this.userService = userService;
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public PageResponse<UserDTO> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return userManagementService.listUsers(name, email, role, active, pageable);
    }

    @PostMapping("/search")
    public PageResponse<UserDTO> search(
            @RequestBody SearchUserRequest request,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return userManagementService.searchUsers(request, pageable);
    }

    @GetMapping("/me")
    public UserDTO profile(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    @PutMapping("/me")
    public UserDTO updateProfile(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        return userManagementService.updateProfile(authentication, request);
    }

    @PostMapping("/change-password")
    public void changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
        userManagementService.changePassword(authentication, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deactivate(@PathVariable Long id) {
        userManagementService.deactivateUser(id);
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable Long id) {
        return userService.findById(id);
    }
}
