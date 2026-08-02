package com.socialapp.ms_auth_users.controller;

import com.socialapp.ms_auth_users.entity.User;
import com.socialapp.ms_auth_users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void getProfile_conUsuarioExistente_deberiaRetornar200ConDatos() {
        User user = User.builder()
                .username("wilson")
                .nombres("Wilson")
                .apellidos("Rodriguez")
                .fechaNacimiento(LocalDate.of(1995, 5, 20))
                .alias("wilsonr")
                .build();

        when(userRepository.findByUsername("wilson")).thenReturn(Optional.of(user));

        Authentication auth = new UsernamePasswordAuthenticationToken("wilson", null);
        ResponseEntity<?> response = userController.getProfile(auth);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getProfile_conUsuarioInexistente_deberiaRetornar404() {
        when(userRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        Authentication auth = new UsernamePasswordAuthenticationToken("fantasma", null);
        ResponseEntity<?> response = userController.getProfile(auth);

        assertEquals(404, response.getStatusCode().value());
    }
}