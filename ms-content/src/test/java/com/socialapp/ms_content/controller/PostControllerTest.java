package com.socialapp.ms_content.controller;

import com.socialapp.ms_content.dto.CreatePostRequest;
import com.socialapp.ms_content.entity.Post;
import com.socialapp.ms_content.repository.PostRepository;
import com.socialapp.ms_content.security.AuthenticatedUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(postController, "entityManager", entityManager);
    }

    @Test
    void listPosts_deberiaRetornar200ConListaDePosts() {
        Post post = Post.builder()
                .id(1L)
                .userId(1L)
                .username("wilson")
                .message("Hola mundo")
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(post));

        ResponseEntity<List<com.socialapp.ms_content.dto.PostResponse>> response = postController.listPosts();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("wilson", response.getBody().get(0).username());
    }

    @Test
    void listPosts_sinPublicaciones_deberiaRetornarListaVacia() {
        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of());

        ResponseEntity<List<com.socialapp.ms_content.dto.PostResponse>> response = postController.listPosts();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void createPost_conUsuarioAutenticado_deberiaRetornar201() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(1L, "wilson");
        Authentication auth = new UsernamePasswordAuthenticationToken(authenticatedUser, null);

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        ResponseEntity<?> response = postController.createPost(new CreatePostRequest("Nuevo post"), auth);

        assertEquals(201, response.getStatusCode().value());
        verify(entityManager).createNativeQuery("CALL content.sp_create_post(?, ?, ?)");
    }
}