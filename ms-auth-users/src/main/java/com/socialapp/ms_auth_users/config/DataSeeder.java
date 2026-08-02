package com.socialapp.ms_auth_users.config;

import com.socialapp.ms_auth_users.entity.User;
import com.socialapp.ms_auth_users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String @NonNull ... args) {
        if (userRepository.count() > 0) {
            return;
        }

        User wilson = crearUsuario("wilson", "Wilson", "Cruz", LocalDate.of(2000, 12, 21), "wacruz");
        User maria = crearUsuario("maria", "Maria", "Gomez", LocalDate.of(1998, 3, 15), "mariag");
        User carlos = crearUsuario("carlos", "Carlos", "Perez", LocalDate.of(1990, 11, 8), "carlosp");


        System.out.println(">>> Usuarios de prueba creados exitosamente");
    }

    private User crearUsuario(String username, String nombres, String apellidos, LocalDate fechaNacimiento, String alias) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode("123456"))
                .nombres(nombres)
                .apellidos(apellidos)
                .fechaNacimiento(fechaNacimiento)
                .alias(alias)
                .build();
        return userRepository.save(user);
    }


}