package dev.yesserm.demosb4.userservice.service.impl;

import dev.yesserm.demosb4.contracts.event.EventMetadata;
import dev.yesserm.demosb4.contracts.event.RoleChangedEvent;
import dev.yesserm.demosb4.contracts.event.UserProfileUpdatedEvent;
import dev.yesserm.demosb4.contracts.pagination.PageResponse;
import dev.yesserm.demosb4.userservice.dto.ChangePasswordRequest;
import dev.yesserm.demosb4.userservice.dto.ChangeRoleRequest;
import dev.yesserm.demosb4.userservice.dto.SearchUserRequest;
import dev.yesserm.demosb4.userservice.dto.UpdateProfileRequest;
import dev.yesserm.demosb4.userservice.dto.UserDTO;
import dev.yesserm.demosb4.userservice.event.DomainEventPublisher;
import dev.yesserm.demosb4.userservice.exception.EmailAlreadyExistsException;
import dev.yesserm.demosb4.userservice.exception.ForbiddenException;
import dev.yesserm.demosb4.userservice.exception.InvalidPasswordException;
import dev.yesserm.demosb4.userservice.exception.RoleNotFoundException;
import dev.yesserm.demosb4.userservice.exception.UserNotFoundException;
import dev.yesserm.demosb4.userservice.model.Role;
import dev.yesserm.demosb4.userservice.model.User;
import dev.yesserm.demosb4.userservice.repository.RoleRepository;
import dev.yesserm.demosb4.userservice.repository.UserRepository;
import dev.yesserm.demosb4.userservice.service.UserManagementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DomainEventPublisher eventPublisher;

    public UserManagementServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            DomainEventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
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

        UserDTO dto = UserMapper.toDto(user);
        eventPublisher.publish(new UserProfileUpdatedEvent(
                UUID.randomUUID(),
                user.getId().toString(),
                Instant.now(),
                new EventMetadata(null, null, null, "user-service"),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatar(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toUnmodifiableSet()),
                authentication.getName()
        ));
        return dto;
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
        Set<String> previousRoles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toUnmodifiableSet());
        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new RoleNotFoundException(request.role()));

        user.setRoles(Set.of(role));
        UserDTO dto = UserMapper.toDto(user);
        eventPublisher.publish(new RoleChangedEvent(
                UUID.randomUUID(),
                user.getId().toString(),
                Instant.now(),
                new EventMetadata(null, null, null, "user-service"),
                user.getId(),
                user.getEmail(),
                previousRoles,
                Set.of(role.getName()),
                authentication.getName()
        ));
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDTO> listUsers(String name, String email, String role, Boolean active, Pageable pageable) {
        Page<UserDTO> users = userRepository.findFiltered(blankToNull(name), blankToNull(email), blankToNull(role), active, pageable)
                .map(UserMapper::toDto);
        return pageResponse(users);
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

        return pageResponse(users);
    }

    private User currentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(authentication.getName()));
    }

    private PageResponse<UserDTO> pageResponse(Page<UserDTO> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
