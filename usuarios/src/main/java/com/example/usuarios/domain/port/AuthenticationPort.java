package com.example.usuarios.domain.port;

public interface AuthenticationPort {
    void authenticate(String email, String password);
}
