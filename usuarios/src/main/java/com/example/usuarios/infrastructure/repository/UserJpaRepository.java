package com.example.usuarios.infrastructure.repository;

import com.example.usuarios.infrastructure.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Integer> {
    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    Optional<UserJpaEntity> findByEmail(String email);
}
