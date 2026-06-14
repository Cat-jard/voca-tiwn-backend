package com.example.usuarios.domain.port;

import com.example.usuarios.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    boolean existsById(Integer id);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    void deleteById(Integer id);
}
