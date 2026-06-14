package com.example.usuarios.infrastructure.controller;

import com.example.usuarios.application.object.LoginResult;
import com.example.usuarios.application.usecase.*;
import com.example.usuarios.domain.model.TipoUser;
import com.example.usuarios.domain.model.User;
import com.example.usuarios.infrastructure.web.dtos.UserLoginRequestDTO;
import com.example.usuarios.infrastructure.web.dtos.UserRegistroDTO;
import com.example.usuarios.infrastructure.web.dtos.UserResponseDTO;
import com.example.usuarios.infrastructure.web.mapper.UserResponseMapper;
import com.example.usuarios.infrastructure.web.mapper.UserWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {
    private final LoginUseCase loginUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ListUsersUseCase listUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO request) {
        LoginResult result = loginUseCase.login(request.email(), request.password());
        return ResponseEntity.ok(UserResponseMapper.toResponse(result.user(), result.token()));
    }

    @PostMapping("/registro")
    public ResponseEntity<UserResponseDTO> registrar(@Valid @RequestBody UserRegistroDTO dto) {
        User user = registerUserUseCase.register(UserWebMapper.toRegistroCommand(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseMapper.toResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> actualizar(@PathVariable Integer id, @RequestBody UserRegistroDTO dto, @RequestParam(required = false) TipoUser tipo) {
        User user = updateUserUseCase.update(id, UserWebMapper.toActualizarCommand(dto, tipo));
        return ResponseEntity.ok(UserResponseMapper.toResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        deleteUserUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> buscarPorId(@PathVariable Integer id) {
        User user = getUserUseCase.get(id);
        return ResponseEntity.ok(UserResponseMapper.toResponse(user));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> listarTodos() {
        List<UserResponseDTO> response = listUserUseCase.listAll().stream().map(UserResponseMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }

}
