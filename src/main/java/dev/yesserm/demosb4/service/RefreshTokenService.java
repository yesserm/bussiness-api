package dev.yesserm.demosb4.service;

import dev.yesserm.demosb4.model.RefreshToken;
import dev.yesserm.demosb4.model.User;

public interface RefreshTokenService {
    RefreshToken create(User user);
    RefreshToken validate(String token);
    RefreshToken rotate(String token);
}
