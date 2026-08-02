package com.socialapp.ms_auth_users.controller;

import com.socialapp.ms_auth_users.dto.ProfileResponse;
import com.socialapp.ms_auth_users.entity.User;
import com.socialapp.ms_auth_users.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        ProfileResponse profile = new ProfileResponse(
                user.getUsername(),
                user.getNombres(),
                user.getApellidos(),
                user.getFechaNacimiento(),
                user.getAlias()
        );

        return ResponseEntity.ok(profile);
    }
}