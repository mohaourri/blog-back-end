package com.example.blogbackend.Service;

import com.example.blogbackend.Dto.CommentRequestDTO;
import com.example.blogbackend.Dto.CommentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

public interface CommentService {
    CommentResponseDTO addComment(Jwt jwt, CommentRequestDTO dto);
    Page<CommentResponseDTO> getCommentsByArticle(Long articleId, int page, int size);
    void deleteComment(Jwt jwt, Long commentId);
}