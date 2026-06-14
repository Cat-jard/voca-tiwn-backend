package com.example.usuarios.application.usecase;

import com.example.usuarios.domain.exceptions.ConflictException;
import com.example.usuarios.domain.model.TipoUser;
import com.example.usuarios.domain.model.User;
import com.example.usuarios.domain.port.UserRepository;
import com.example.usuarios.infrastructure.web.commands.RegistroUserCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegistroUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ConflictException("El email ya está registrado");
        }
        if (userRepository.existsByDni(command.dni())) {
            throw new ConflictException("El DNI ya está registrado");
        }
        User user = new User(
                null,
                command.nombre(),
                command.apellido(),
                command.dni(),
                command.email(),
                passwordEncoder.encode(command.password()),
                TipoUser.POSTULANTE
        );
        return userRepository.save(user);
    }
}
