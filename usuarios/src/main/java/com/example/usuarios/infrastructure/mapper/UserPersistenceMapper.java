package com.example.usuarios.infrastructure.mapper;

import com.example.usuarios.domain.model.User;
import com.example.usuarios.infrastructure.entity.UserJpaEntity;

public final class UserPersistenceMapper {
    private UserPersistenceMapper() {
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null)
            return null;
        return new User(
                entity.getIdUsuario(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getDni(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getTipo()
        );
    }

    public static UserJpaEntity toEntity(User user) {
        if (user == null)
            return null;
        return UserJpaEntity.builder()
                .idUsuario(user.userId())
                .nombre(user.nombre())
                .apellido(user.apellido())
                .dni(user.dni())
                .email(user.email())
                .password(user.password())
                .tipo(user.tipoUser())
                .build();
    }
}
