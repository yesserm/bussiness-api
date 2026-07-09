package dev.yesserm.demosb4.authservice.service;

import dev.yesserm.demosb4.authservice.model.RefreshToken;
import dev.yesserm.demosb4.authservice.model.User;

public interface RefreshTokenService {

    RefreshToken create(User user);

    RefreshToken rotate(String token);
}
