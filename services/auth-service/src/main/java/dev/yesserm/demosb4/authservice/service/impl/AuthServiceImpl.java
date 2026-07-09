package dev.yesserm.demosb4.authservice.service.impl;

import dev.yesserm.demosb4.authservice.dto.LoginRequest;
import dev.yesserm.demosb4.authservice.dto.LoginResponse;
import dev.yesserm.demosb4.authservice.dto.RefreshTokenRequest;
import dev.yesserm.demosb4.authservice.dto.RegisterRequest;
import dev.yesserm.demosb4.authservice.dto.TokenDTO;
import dev.yesserm.demosb4.authservice.dto.UserDTO;
import dev.yesserm.demosb4.authservice.exception.EmailAlreadyExistsException;
import dev.yesserm.demosb4.authservice.exception.ForbiddenException;
import dev.yesserm.demosb4.authservice.exception.InvalidCredentialsException;
import dev.yesserm.demosb4.authservice.model.RefreshToken;
import dev.yesserm.demosb4.authservice.model.Role;
import dev.yesserm.demosb4.authservice.model.User;
import dev.yesserm.demosb4.authservice.repository.RoleRepository;
import dev.yesserm.demosb4.authservice.repository.UserRepository;
import dev.yesserm.demosb4.authservice.service.AuthService;
import dev.yesserm.demosb4.authservice.service.JwtService;
import dev.yesserm.demosb4.authservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final String adminSetupKey;

    AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            @Value("${app.security.admin-setup-key:}") String adminSetupKey
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.adminSetupKey = adminSetupKey;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        return buildResponse(user, refreshTokenService.create(user));
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        return registerWithRole(request, "USER");
    }

    @Override
    @Transactional
    public LoginResponse registerAdmin(RegisterRequest request, String setupKey) {
        if (adminSetupKey == null || adminSetupKey.isBlank() || !adminSetupKey.equals(setupKey)) {
            throw new ForbiddenException("Invalid admin setup key");
        }
        return registerWithRole(request, "ADMIN");
    }

    @Override
    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.rotate(request.refreshToken());
        return buildResponse(refreshToken.getUser(), refreshToken);
    }

    private LoginResponse registerWithRole(RegisterRequest request, String roleName) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Default " + roleName + " role is missing"));

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        user.setActive(true);
        user.setRoles(Set.of(role));

        User saved = userRepository.save(user);
        return buildResponse(saved, refreshTokenService.create(saved));
    }

    private LoginResponse buildResponse(User user, RefreshToken refreshToken) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .toList())
                .disabled(!user.isEnabled() || !user.isActive())
                .build();

        TokenDTO tokens = new TokenDTO(
                jwtService.generateToken(userDetails),
                refreshToken.getToken(),
                "Bearer",
                jwtService.accessTokenExpiration()
        );

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isActive(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toUnmodifiableSet())
        );

        return new LoginResponse(tokens, userDTO);
    }
}
