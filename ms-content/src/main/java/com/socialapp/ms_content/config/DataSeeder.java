package com.socialapp.ms_content.config;

import com.socialapp.ms_content.entity.Post;
import com.socialapp.ms_content.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PostRepository postRepository;

    @Override
    public void run(String... args) {
        if (postRepository.count() > 0) {
            return;
        }

        postRepository.save(Post.builder().userId(1L).username("wilson")
                .message("¡Mi primera publicación en la red social!").likeCount(0).build());

        postRepository.save(Post.builder().userId(2L).username("maria")
                .message("Hola a todos, feliz de estar aquí.").likeCount(0).build());

        postRepository.save(Post.builder().userId(3L).username("carlos")
                .message("Probando esta nueva plataforma.").likeCount(0).build());

        postRepository.save(Post.builder().userId(4L).username("ana")
                .message("¿Alguien más emocionado por esto?").likeCount(0).build());

        System.out.println(">>> Publicaciones de prueba creadas en ms-content");
    }
}