package dev.yesserm.demosb4.userservice.service;

import dev.yesserm.demosb4.contracts.pagination.PageResponse;
import dev.yesserm.demosb4.userservice.dto.ChangePasswordRequest;
import dev.yesserm.demosb4.userservice.dto.ChangeRoleRequest;
import dev.yesserm.demosb4.userservice.dto.SearchUserRequest;
import dev.yesserm.demosb4.userservice.dto.UpdateProfileRequest;
import dev.yesserm.demosb4.userservice.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface UserManagementService {
    void changePassword(Authentication authentication, ChangePasswordRequest request);

    UserDTO updateProfile(Authentication authentication, UpdateProfileRequest request);

    void deactivateUser(Long id);

    UserDTO changeRole(Long id, ChangeRoleRequest request, Authentication authentication);

    PageResponse<UserDTO> listUsers(String name, String email, String role, Boolean active, Pageable pageable);

    PageResponse<UserDTO> searchUsers(SearchUserRequest request, Pageable pageable);
}
