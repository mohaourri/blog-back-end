package com.example.blogbackend.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArticleRequestDTO {

    @NotBlank(message = "Le titre ne peut pas être vide")
    @Size(min = 5, max = 100, message = "Le titre doit contenir entre 5 et 100 caractères")
    private String title;

    @NotBlank(message = "Le contenu ne peut pas être vide")
    @Size(min = 10, message = "Le contenu doit contenir au moins 10 caractères")
    private String content;

}