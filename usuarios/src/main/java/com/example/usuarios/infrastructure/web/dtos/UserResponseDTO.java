package com.example.usuarios.infrastructure.web.dtos;

import com.example.usuarios.domain.model.TipoUser;

public record UserResponseDTO(
        Integer idUsuario,
        String nombre,
        String apellido,
        String dni,
        String email,
        TipoUser tipo,
        String token
) {
}
