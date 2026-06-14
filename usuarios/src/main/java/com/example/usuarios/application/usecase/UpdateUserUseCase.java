package com.example.usuarios.application.usecase;

import com.example.usuarios.domain.exceptions.ResourceNotFoundException;
import com.example.usuarios.domain.model.User;
import com.example.usuarios.domain.port.UserRepository;
import com.example.usuarios.infrastructure.web.commands.ActualizarUserCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User update(Integer id, ActualizarUserCommand command) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con la id: " + id));
        User updated = new User(
                user.userId(),
                command.nombre(),
                command.apellido(),
                command.dni(),
                command.email(),
                command.password() == null ?
                        user.password() : passwordEncoder.encode(command.password()), command.tipo());
        return userRepository.save(updated);
    }
}