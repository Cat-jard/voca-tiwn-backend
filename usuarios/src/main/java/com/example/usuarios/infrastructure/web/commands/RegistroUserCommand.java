package com.example.usuarios.infrastructure.web.commands;

public record RegistroUserCommand(
        String nombre,
        String apellido,
        String dni,
        String email,
        String password
) {
}
