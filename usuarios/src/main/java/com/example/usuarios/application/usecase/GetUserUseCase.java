package com.example.usuarios.application.usecase;

import com.example.usuarios.domain.exceptions.ResourceNotFoundException;
import com.example.usuarios.domain.model.User;
import com.example.usuarios.domain.port.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class GetUserUseCase {
    private final UserRepository userRepository;

    public User get(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + id));
    }
}
