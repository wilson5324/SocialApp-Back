package com.socialapp.ms_auth_users.controller;

import com.socialapp.ms_auth_users.dto.LoginRequest;
import com.socialapp.ms_auth_users.entity.User;
import com.socialapp.ms_auth_users.repository.UserRepository;
import com.socialapp.ms_auth_users.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_conCredencialesValidas_deberiaRetornar200ConToken() {
        User user = User.builder()
                .id(1L)
                .username("wilson")
                .password("encoded-password")
                .alias("wilsonr")
                .build();

        when(userRepository.findByUsername("wilson")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(true);
        when(jwtUtil.generateToken(1L,"wilson")).thenReturn("fake-jwt-token");

        ResponseEntity<?> response = authController.login(new LoginRequest("wilson", "123456"));

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void login_conPasswordIncorrecto_deberiaRetornar401() {
        User user = User.builder()
                .id(1L)
                .username("wilson")
                .password("encoded-password")
                .build();

        when(userRepository.findByUsername("wilson")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        ResponseEntity<?> response = authController.login(new LoginRequest("wilson", "wrong-password"));

        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void login_conUsuarioInexistente_deberiaRetornar401() {
        when(userRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(new LoginRequest("noexiste", "123456"));

        assertEquals(401, response.getStatusCode().value());
    }
}