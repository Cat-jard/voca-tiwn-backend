package com.example.usuarios.application.usecase;

import com.example.usuarios.domain.exceptions.ResourceNotFoundException;
import com.example.usuarios.domain.port.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteUserUseCase {
    private final UserRepository userRepository;

    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario con encontrado con el id: " + id);
        }
        userRepository.deleteById(id);
    }
}
