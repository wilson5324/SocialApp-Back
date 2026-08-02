package com.socialapp.ms_content.dto;

import java.time.LocalDateTime;


public record PostResponse(
        Long id,
        Long userId,
        String username,
        String message,
        Integer likeCount,
        LocalDateTime createdAt
) {}