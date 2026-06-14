package com.example.usuarios.application.usecase;

import com.example.usuarios.domain.model.User;
import com.example.usuarios.domain.port.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ListUsersUseCase {
    private final UserRepository userRepository;

    public List<User> listAll() {
        return userRepository.findAll();
    }
}
