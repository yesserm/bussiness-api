package dev.yesserm.demosb4.service.impl;

import dev.yesserm.demosb4.dto.ChangePasswordRequest;
import dev.yesserm.demosb4.dto.ChangeRoleRequest;
import dev.yesserm.demosb4.dto.PageResponse;
import dev.yesserm.demosb4.dto.SearchUserRequest;
import dev.yesserm.demosb4.dto.UpdateProfileRequest;
import dev.yesserm.demosb4.dto.UserDTO;
import dev.yesserm.demosb4.exception.EmailAlreadyExistsException;
import dev.yesserm.demosb4.exception.ForbiddenException;
import dev.yesserm.demosb4.exception.InvalidPasswordException;
import dev.yesserm.demosb4.exception.RoleNotFoundException;
import dev.yesserm.demosb4.exception.UserNotFoundException;
import dev.yesserm.demosb4.model.Role;
import dev.yesserm.demosb4.model.User;
import dev.yesserm.demosb4.repository.RoleRepository;
import dev.yesserm.demosb4.repository.UserRepository;
import dev.yesserm.demosb4.service.UserManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void changePassword(Authentication authentication, ChangePasswordRequest request) {
        User user = currentUser(authentication);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is invalid");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new InvalidPasswordException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
    }

    @Override
    @Transactional
    public UserDTO updateProfile(Authentication authentication, UpdateProfileRequest request) {
        User user = currentUser(authentication);

        if (userRepository.existsByEmailAndIdNot(request.email(), user.getId())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setAvatar(request.avatar());

        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setActive(false);
    }

    @Override
    @Transactional
    public UserDTO changeRole(Long id, ChangeRoleRequest request, Authentication authentication) {
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));

        if (!isAdmin) {
            throw new ForbiddenException("Only ADMIN users can change roles");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new RoleNotFoundException(request.role()));

        user.setRoles(Set.of(role));
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDTO> listUsers(String name, String email, String role, Boolean active, Pageable pageable) {
        Page<UserDTO> users = userRepository.findFiltered(blankToNull(name), blankToNull(email), blankToNull(role), active, pageable)
                .map(UserMapper::toDto);
        return PageResponse.from(users);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDTO> searchUsers(SearchUserRequest request, Pageable pageable) {
        List<String> roles = request.roles() == null
                ? List.of()
                : request.roles().stream().filter(role -> role != null && !role.isBlank()).toList();

        Collection<String> roleParam = roles.isEmpty() ? List.of("__NO_ROLE__") : roles;
        Page<UserDTO> users = userRepository.search(
                        blankToNull(request.name()),
                        blankToNull(request.email()),
                        roleParam,
                        roles.isEmpty(),
                        request.active(),
                        request.createdFrom(),
                        request.createdTo(),
                        pageable
                )
                .map(UserMapper::toDto);

        return PageResponse.from(users);
    }

    private User currentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(authentication.getName()));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
