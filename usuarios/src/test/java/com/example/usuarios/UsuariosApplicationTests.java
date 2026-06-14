package com.example.usuarios;

import com.example.usuarios.application.object.LoginResult;
import com.example.usuarios.application.usecase.*;
import com.example.usuarios.domain.exceptions.ConflictException;
import com.example.usuarios.domain.exceptions.ResourceNotFoundException;
import com.example.usuarios.domain.model.TipoUser;
import com.example.usuarios.domain.model.User;
import com.example.usuarios.domain.port.UserRepository;
import com.example.usuarios.infrastructure.adapter.JwtAdapter;
import com.example.usuarios.infrastructure.web.commands.ActualizarUserCommand;
import com.example.usuarios.infrastructure.web.commands.RegistroUserCommand;
import com.example.usuarios.infrastructure.web.dtos.UserLoginRequestDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
class UsuariosApplicationTests {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Autowired
    private ListUsersUseCase listUsersUseCase;

    @Autowired
    private UpdateUserUseCase updateUserUseCase;

    @Autowired
    private DeleteUserUseCase deleteUserUseCase;

    @Autowired
    private JwtAdapter jwtAdapter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String generateToken(String username, List<String> authorities, Date expiration) {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .subject(username)
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private RegistroUserCommand buildRegisterCommand() {
        return new RegistroUserCommand(
                "Kenji",
                "Perez",
                "44411122",
                "kenji@test.com",
                "password123"
        );
    }

    private UserLoginRequestDTO buildLoginDTO() {
        return new UserLoginRequestDTO(
                "kenji@test.com",
                "password123"
        );
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldRegisterUser() {
        User saved = registerUserUseCase.register(buildRegisterCommand());
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.userId()).isNotNull();
        Assertions.assertThat(saved.nombre()).isEqualTo("Kenji");
        Assertions.assertThat(saved.apellido()).isEqualTo("Perez");
        Assertions.assertThat(saved.email()).isEqualTo("kenji@test.com");
        Assertions.assertThat(saved.tipoUser()).isEqualTo(TipoUser.POSTULANTE);

        User user = userRepository.findByEmail(saved.email()).orElseThrow();

        Assertions.assertThat(passwordEncoder.matches("password123", user.password())).isTrue();
    }

    @Test
    void shouldLoginUser() {
        registerUserUseCase.register(
                buildRegisterCommand()
        );
        LoginResult result = loginUseCase.login("kenji@test.com", "password123");

        Assertions.assertThat(result).isNotNull();

        Assertions.assertThat(result.user().email()).isEqualTo("kenji@test.com");

        Assertions.assertThat(result.token()).isNotBlank();

    }

    @Test
    void shouldFindUserById() {
        User saved = registerUserUseCase.register(buildRegisterCommand());

        User found = getUserUseCase.get(saved.userId());

        Assertions.assertThat(found).isNotNull();

        Assertions.assertThat(found.userId()).isEqualTo(saved.userId());

        Assertions.assertThat(found.email()).isEqualTo(saved.email());
    }

    @Test
    void shouldListAllUsers() {
        registerUserUseCase.register(buildRegisterCommand());

        List<User> users = listUsersUseCase.listAll();

        Assertions.assertThat(users).isNotEmpty();
    }

    @Test
    void shouldUpdateUser() {
        User saved = registerUserUseCase.register(buildRegisterCommand());

        ActualizarUserCommand command = new ActualizarUserCommand(
                "Mario",
                "Gomez",
                "66611122",
                "mario@test.com",
                "newpassword123",
                TipoUser.POSTULANTE
        );

        User updated = updateUserUseCase.update(saved.userId(), command);
        Assertions.assertThat(updated.nombre()).isEqualTo("Mario");

        Assertions.assertThat(updated.apellido()).isEqualTo("Gomez");

        Assertions.assertThat(updated.email()).isEqualTo("mario@test.com");

        Assertions.assertThat(updated.tipoUser()).isEqualTo(TipoUser.POSTULANTE);

        User dbUser = userRepository.findById(saved.userId()).orElseThrow();
        Assertions.assertThat(passwordEncoder.matches("newpassword123", dbUser.password())).isTrue();
    }

    @Test
    void shouldDeleteUser() {
        User saved = registerUserUseCase.register(buildRegisterCommand());
        deleteUserUseCase.delete(saved.userId());
        Assertions.assertThat(userRepository.findById(saved.userId())).isEmpty();
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        RegistroUserCommand command = buildRegisterCommand();

        registerUserUseCase.register(command);

        Assertions.assertThatThrownBy(() -> registerUserUseCase.register(command))
                .isInstanceOf(ConflictException.class).hasMessageContaining("email ya está registrado");
    }

    @Test
    void shouldThrowWhenDniAlreadyExists() {
        registerUserUseCase.register(
                buildRegisterCommand()
        );

        RegistroUserCommand second = new RegistroUserCommand(
                "Maria",
                "Lopez",
                "44411122",
                "maria@test.com",
                "password123"
        );

        Assertions.assertThatThrownBy(() -> registerUserUseCase.register(second))
                .isInstanceOf(ConflictException.class).hasMessageContaining("DNI ya está registrado");
    }

    @Test
    void shouldThrowWhenUserNotFoundById() {
        Assertions.assertThatThrownBy(() -> getUserUseCase.get(99999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingUser() {
        ActualizarUserCommand command = new ActualizarUserCommand(
                "Mario",
                "Gomez",
                "66611122",
                "mario@test.com",
                "password123",
                TipoUser.POSTULANTE
        );
        Assertions.assertThatThrownBy(() -> updateUserUseCase.update(99999, command)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingUser() {
        Assertions.assertThatThrownBy(() -> deleteUserUseCase.delete(99999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldGenerateValidJwtToken() {
        User user = registerUserUseCase.register(buildRegisterCommand());

        String token = jwtAdapter.generateToken(user);

        Assertions.assertThat(token).isNotBlank();

        Assertions.assertThat(jwtAdapter.isTokenValid(token)).isTrue();

        Assertions.assertThat(jwtAdapter.getUsernameFromToken(token)).isEqualTo(user.email());
    }

    @Test
    void shouldAuthenticateWithAuthenticationManager() {
        registerUserUseCase.register(
                buildRegisterCommand()
        );
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                "kenji@test.com",
                "password123"));

        Assertions.assertThat(authentication.isAuthenticated()).isTrue();
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        registerUserUseCase.register(
                buildRegisterCommand()
        );
        Assertions.assertThatThrownBy(() -> loginUseCase.login("kenji@test.com", "wrongpassword"))
                .isInstanceOf(Exception.class);
    }

    @Test
    void shouldUpdateUserWithoutChangingPassword() {
        User saved = registerUserUseCase.register(buildRegisterCommand());

        User existing = userRepository.findById(saved.userId()).orElseThrow();

        String oldPassword = existing.password();

        ActualizarUserCommand command = new ActualizarUserCommand(
                "Kenji Updated",
                "Perez Updated",
                "33388822",
                "updated@test.com",
                null,
                TipoUser.POSTULANTE
        );

        updateUserUseCase.update(saved.userId(), command);

        User updated = userRepository.findById(saved.userId()).orElseThrow();

        Assertions.assertThat(updated.password()).isEqualTo(oldPassword);
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String token = generateToken("kenji@test.com", List.of("POSTULANTE"),
                new Date(System.currentTimeMillis() + 100000)
        );
        String username = jwtAdapter.getUsernameFromToken(token);
        Assertions.assertThat(username).isEqualTo("kenji@test.com");
    }

    @Test
    void shouldValidateCorrectToken() {
        String token = generateToken("kenji@test.com", List.of("POSTULANTE"),
                new Date(System.currentTimeMillis() + 100000)
        );
        boolean valid = jwtAdapter.isTokenValid(token);
        Assertions.assertThat(valid).isTrue();
    }

    @Test
    void shouldRejectExpiredToken() {
        String token = generateToken("kenji@test.com", List.of("POSTULANTE"),
                new Date(System.currentTimeMillis() - 100000)
        );
        boolean valid = jwtAdapter.isTokenValid(token);
        Assertions.assertThat(valid).isFalse();
    }

    @Test
    void shouldRejectMalformedToken() {
        boolean valid = jwtAdapter.isTokenValid("bad.token.here");

        Assertions.assertThat(valid).isFalse();
    }
}
