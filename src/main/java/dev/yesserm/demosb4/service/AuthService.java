package dev.yesserm.demosb4.service;

import dev.yesserm.demosb4.dto.LoginRequest;
import dev.yesserm.demosb4.dto.LoginResponse;
import dev.yesserm.demosb4.dto.RefreshTokenRequest;
import dev.yesserm.demosb4.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    LoginResponse refresh(RefreshTokenRequest request);
}
