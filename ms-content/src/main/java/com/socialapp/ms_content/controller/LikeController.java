package com.socialapp.ms_content.controller;

import com.socialapp.ms_content.dto.LikeRequest;
import com.socialapp.ms_content.repository.PostRepository;
import com.socialapp.ms_content.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Tag(name = "Likes")
public class LikeController {

    private final PostRepository postRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping
    @Transactional
    public ResponseEntity<?> addLike(@RequestBody LikeRequest request, Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();

        entityManager.createNativeQuery("CALL content.sp_add_like(?, ?)")
                .setParameter(1, request.postId())
                .setParameter(2, user.userId())
                .executeUpdate();

        Integer newCount = postRepository.findById(request.postId())
                .map(p -> p.getLikeCount())
                .orElse(0);

        Map<String, Object> payload = Map.of("postId", request.postId(), "likeCount", newCount);

        messagingTemplate.convertAndSend("/topic/likes", Optional.of(payload));

        return ResponseEntity.ok().body(payload);
    }
}