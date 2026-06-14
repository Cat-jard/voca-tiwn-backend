package com.example.usuarios.domain.port;

import com.example.usuarios.domain.model.User;

public interface JwtPort {
    String generateToken(User user);
}
