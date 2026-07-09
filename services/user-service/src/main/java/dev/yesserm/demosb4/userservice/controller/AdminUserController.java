package dev.yesserm.demosb4.userservice.controller;

import dev.yesserm.demosb4.userservice.dto.ChangeRoleRequest;
import dev.yesserm.demosb4.userservice.dto.UserDTO;
import dev.yesserm.demosb4.userservice.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {
    private final UserManagementService userManagementService;

    public AdminUserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO changeRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request,
            Authentication authentication
    ) {
        return userManagementService.changeRole(id, request, authentication);
    }
}
