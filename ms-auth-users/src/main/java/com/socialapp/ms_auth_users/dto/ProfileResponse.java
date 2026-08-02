package com.socialapp.ms_auth_users.dto;

import java.time.LocalDate;

public record ProfileResponse(
        String username,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String alias
) {}