package com.example.usuarios.application.object;

import com.example.usuarios.domain.model.User;

public record LoginResult(User user, String token) {
}
