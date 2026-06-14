package com.example.usuarios.infrastructure.web.commands;

import com.example.usuarios.domain.model.TipoUser;

public record ActualizarUserCommand(
        String nombre,
        String apellido,
        String dni,
        String email,
        String password,
        TipoUser tipo
) {
}
