package com.example.blogbackend.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDTO {

    @NotBlank(message = "Le contenu ne peut pas être vide")
    private String content;

    private Long articleId;
}