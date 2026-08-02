package com.socialapp.ms_auth_users.controller;

import com.socialapp.ms_auth_users.dto.LoginRequest;
import com.socialapp.ms_auth_users.dto.LoginResponse;
import com.socialapp.ms_auth_users.entity.User;
import com.socialapp.ms_auth_users.repository.UserRepository;
import com.socialapp.ms_auth_users.security.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getId(),user.getUsername());
        return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getAlias()));
    }
}