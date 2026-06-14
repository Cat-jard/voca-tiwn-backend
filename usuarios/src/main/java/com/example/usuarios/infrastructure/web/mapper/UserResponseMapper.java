package com.example.usuarios.infrastructure.web.mapper;

import com.example.usuarios.domain.model.User;
import com.example.usuarios.infrastructure.web.dtos.UserResponseDTO;

public class UserResponseMapper {
    private UserResponseMapper() {
    }

    public static UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.userId(),
                user.nombre(),
                user.apellido(),
                user.dni(),
                user.email(),
                user.tipoUser(),
                null
        );
    }

    public static UserResponseDTO toResponse(User user, String token) {
        return new UserResponseDTO(
                user.userId(),
                user.nombre(),
                user.apellido(),
                user.dni(),
                user.email(),
                user.tipoUser(),
                token
        );
    }
}
