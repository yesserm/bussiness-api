package dev.yesserm.demosb4.authservice.controller;

import dev.yesserm.demosb4.authservice.dto.LoginRequest;
import dev.yesserm.demosb4.authservice.dto.LoginResponse;
import dev.yesserm.demosb4.authservice.dto.RefreshTokenRequest;
import dev.yesserm.demosb4.authservice.dto.RegisterRequest;
import dev.yesserm.demosb4.authservice.service.AuthService;
import dev.yesserm.demosb4.contracts.http.ServiceHeaders;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public LoginResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/register-admin")
    public LoginResponse registerAdmin(
            @RequestHeader(ServiceHeaders.ADMIN_SETUP_KEY) String setupKey,
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.registerAdmin(request, setupKey);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }
}
