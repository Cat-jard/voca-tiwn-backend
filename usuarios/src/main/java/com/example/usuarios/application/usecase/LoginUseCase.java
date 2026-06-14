package com.example.usuarios.application.usecase;

import com.example.usuarios.application.object.LoginResult;
import com.example.usuarios.domain.port.AuthenticationPort;
import com.example.usuarios.domain.port.JwtPort;
import com.example.usuarios.domain.exceptions.ResourceNotFoundException;
import com.example.usuarios.domain.model.User;
import com.example.usuarios.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
    private final AuthenticationPort authenticationPort;
    private final JwtPort jwtPort;
    private final UserRepository userRepository;

    public LoginResult login(String email, String password) {
        authenticationPort.authenticate(email, password);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el email: " + email));
        String token = jwtPort.generateToken(user);
        return new LoginResult(user, token);
    }
}
