package com.example.blogbackend.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserProfileRequestDTO {

    @NotBlank(message = "La bio ne peut pas être vide")
    private String bio;

    private String avatarUrl;
}