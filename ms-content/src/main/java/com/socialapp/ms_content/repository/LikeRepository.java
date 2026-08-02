package com.socialapp.ms_content.repository;

import com.socialapp.ms_content.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}