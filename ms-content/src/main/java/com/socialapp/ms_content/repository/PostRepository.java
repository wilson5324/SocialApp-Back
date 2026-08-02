package com.socialapp.ms_content.repository;

import com.socialapp.ms_content.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<com.socialapp.ms_content.entity.Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
}