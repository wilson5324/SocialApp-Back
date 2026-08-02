package com.socialapp.ms_auth_users.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "test-secret-key-para-pruebas-unitarias-1234567890");
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);
    }

    @Test
    void generateToken_deberiaGenerarTokenValido() {
        String token = jwtUtil.generateToken( 1L,"wilson");

        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void extractUsername_deberiaRetornarElUsernameCorrecto() {
        String token = jwtUtil.generateToken(1L,"wilson");

        String username = jwtUtil.extractUsername(token);

        assertEquals("wilson", username);
    }

    @Test
    void extractUserId_deberiaRetornarElIdCorrecto() {
        String token = jwtUtil.generateToken(1L,"wilson");

        Long userId = jwtUtil.extractUserId(token);

        assertEquals(1L, userId);
    }

    @Test
    void isTokenValid_deberiaRetornarFalseParaTokenInvalido() {
        boolean valid = jwtUtil.isTokenValid("token-invalido-y-malformado");

        assertFalse(valid);
    }
}