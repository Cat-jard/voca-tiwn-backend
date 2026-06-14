package com.example.usuarios.infrastructure.web.mapper;

import com.example.usuarios.domain.model.TipoUser;
import com.example.usuarios.infrastructure.web.commands.ActualizarUserCommand;
import com.example.usuarios.infrastructure.web.commands.RegistroUserCommand;
import com.example.usuarios.infrastructure.web.dtos.UserRegistroDTO;

public final class UserWebMapper {
    private UserWebMapper() {
    }

    public static RegistroUserCommand toRegistroCommand(UserRegistroDTO request) {
        return new RegistroUserCommand(
                request.nombre(),
                request.apellido(),
                request.dni(),
                request.email(),
                request.password()
        );
    }

    public static ActualizarUserCommand toActualizarCommand(UserRegistroDTO request, TipoUser tipo) {
        return new ActualizarUserCommand(
                request.nombre(),
                request.apellido(),
                request.dni(),
                request.email(),
                request.password(),
                tipo
        );
    }
}
