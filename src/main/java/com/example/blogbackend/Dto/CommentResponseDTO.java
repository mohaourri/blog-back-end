package com.example.blogbackend.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentResponseDTO {

    private Long id;
    private String content;
    private String authorUsername;
    private Long articleId;
    private LocalDateTime createdAt;
}