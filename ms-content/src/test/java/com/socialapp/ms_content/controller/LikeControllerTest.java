package com.socialapp.ms_content.controller;

import com.socialapp.ms_content.dto.LikeRequest;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private LikeController likeController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(likeController, "entityManager", entityManager);
    }

    @Test
    void addLike_conUsuarioAutenticado_deberiaRetornar200YPublicarEnWebSocket() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(1L, "wilson");
        Authentication auth = new UsernamePasswordAuthenticationToken(authenticatedUser, null);

        Post post = Post.builder()
                .id(1L)
                .userId(2L)
                .username("maria")
                .message("Hola")
                .likeCount(5)
                .createdAt(LocalDateTime.now())
                .build();

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        ResponseEntity<?> response = likeController.addLike(new LikeRequest(1L), auth);

        assertEquals(200, response.getStatusCode().value());
        verify(entityManager).createNativeQuery("CALL content.sp_add_like(?, ?)");
        verify(messagingTemplate).convertAndSend(eq("/topic/likes"), any(Object.class));
    }

    @Test
    void addLike_conPostInexistente_deberiaRetornarLikeCountCero() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(1L, "wilson");
        Authentication auth = new UsernamePasswordAuthenticationToken(authenticatedUser, null);

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyInt(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = likeController.addLike(new LikeRequest(999L), auth);

        assertEquals(200, response.getStatusCode().value());
        verify(messagingTemplate).convertAndSend(eq("/topic/likes"), any(Object.class));
    }
}