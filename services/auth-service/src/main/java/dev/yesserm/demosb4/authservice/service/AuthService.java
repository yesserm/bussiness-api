package dev.yesserm.demosb4.authservice.service;

import dev.yesserm.demosb4.authservice.dto.LoginRequest;
import dev.yesserm.demosb4.authservice.dto.LoginResponse;
import dev.yesserm.demosb4.authservice.dto.RefreshTokenRequest;
import dev.yesserm.demosb4.authservice.dto.RegisterRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse register(RegisterRequest request);

    LoginResponse registerAdmin(RegisterRequest request, String setupKey);

    LoginResponse refresh(RefreshTokenRequest request);
}
