package com.example.usuarios.domain.model;

import com.example.usuarios.domain.exceptions.DomainValidationException;

public record User(
        Integer userId,
        String nombre,
        String apellido,
        String dni,
        String email,
        String password,
        TipoUser tipoUser
) {
    public User {
        if (nombre == null || nombre.isBlank()) {
            throw new DomainValidationException("Nombre obligatiorio");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new DomainValidationException("Apellido obligatiorio");
        }
        if (dni == null || dni.isBlank()) {
            throw new DomainValidationException("DNI obligatiorio");
        }
        if (!dni.matches("\\d{8}")) {
            throw new DomainValidationException("DNI debe tener 8 dígitos");
        }
        if (email == null || email.isBlank()) {
            throw new DomainValidationException("Email obligatiorio");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new DomainValidationException("Email inválido");
        }
        if (password == null || password.isBlank()) {
            throw new DomainValidationException("contraseña obligatioria");
        }
        if (tipoUser == null) {
            throw new DomainValidationException("Se debe especificar un tipo de usuario obligatioriamente");
        }
    }
}
