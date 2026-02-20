package com.example.blogbackend.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDTO {

    @NotBlank(message = "Le contenu ne peut pas être vide")
    @Size(min = 2, max = 500, message = "Le commentaire doit contenir entre 2 et 500 caractères")
    private String content;

    @NotNull(message = "L'identifiant de l'article est obligatoire")
    private Long articleId;
}