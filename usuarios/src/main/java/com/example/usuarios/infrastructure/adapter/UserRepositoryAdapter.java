package com.example.usuarios.infrastructure.adapter;

import com.example.usuarios.domain.model.User;
import com.example.usuarios.domain.port.UserRepository;
import com.example.usuarios.infrastructure.entity.UserJpaEntity;
import com.example.usuarios.infrastructure.mapper.UserPersistenceMapper;
import com.example.usuarios.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository repository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserPersistenceMapper.toEntity(user);
        entity = repository.save(entity);
        return UserPersistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return repository.findById(id).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(UserPersistenceMapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        return repository.existsByDni(dni);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
