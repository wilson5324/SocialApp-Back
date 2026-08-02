package com.socialapp.ms_content.controller;

import com.socialapp.ms_content.dto.CreatePostRequest;
import com.socialapp.ms_content.dto.PostResponse;
import com.socialapp.ms_content.repository.PostRepository;
import com.socialapp.ms_content.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts")
public class PostController {

    private final PostRepository postRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public ResponseEntity<List<PostResponse>> listPosts() {
        List<PostResponse> posts = postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(p -> new PostResponse(p.getId(), p.getUserId(), p.getUsername(),
                        p.getMessage(), p.getLikeCount(), p.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(posts);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request, Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();

        entityManager.createNativeQuery("CALL content.sp_create_post(?, ?, ?)")
                .setParameter(1, user.userId())
                .setParameter(2, user.username())
                .setParameter(3, request.message())
                .executeUpdate();

        return ResponseEntity.status(201).body("Publicación creada");
    }
}