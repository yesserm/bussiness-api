package dev.yesserm.demosb4.service;

import dev.yesserm.demosb4.dto.ChangePasswordRequest;
import dev.yesserm.demosb4.dto.ChangeRoleRequest;
import dev.yesserm.demosb4.dto.PageResponse;
import dev.yesserm.demosb4.dto.SearchUserRequest;
import dev.yesserm.demosb4.dto.UpdateProfileRequest;
import dev.yesserm.demosb4.dto.UserDTO;
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
