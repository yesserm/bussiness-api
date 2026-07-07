package dev.yesserm.demosb4.service.impl;

import dev.yesserm.demosb4.dto.LoginRequest;
import dev.yesserm.demosb4.dto.LoginResponse;
import dev.yesserm.demosb4.dto.RefreshTokenRequest;
import dev.yesserm.demosb4.dto.RegisterRequest;
import dev.yesserm.demosb4.dto.TokenDTO;
import dev.yesserm.demosb4.dto.UserDTO;
import dev.yesserm.demosb4.exception.InvalidCredentialsException;
import dev.yesserm.demosb4.model.RefreshToken;
import dev.yesserm.demosb4.model.Role;
import dev.yesserm.demosb4.model.User;
import dev.yesserm.demosb4.repository.RoleRepository;
import dev.yesserm.demosb4.repository.UserRepository;
import dev.yesserm.demosb4.service.AuthService;
import dev.yesserm.demosb4.service.JwtService;
import dev.yesserm.demosb4.service.RefreshTokenService;
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
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
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
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default USER role is missing"));

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

    @Override
    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.rotate(request.refreshToken());
        return buildResponse(refreshToken.getUser(), refreshToken);
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

        UserDTO userDTO = UserMapper.toDto(user);

        return new LoginResponse(tokens, userDTO);
    }
}
