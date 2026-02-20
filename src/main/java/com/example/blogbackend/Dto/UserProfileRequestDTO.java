package com.example.blogbackend.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileRequestDTO {

    @NotBlank(message = "La bio ne peut pas être vide")
    @Size(min = 10, max = 300, message = "La bio doit contenir entre 10 et 300 caractères")
    private String bio;


    private String avatarUrl;
}