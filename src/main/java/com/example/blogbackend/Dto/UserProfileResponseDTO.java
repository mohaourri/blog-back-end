package com.example.blogbackend.Dto;


import lombok.Data;

@Data
public class UserProfileResponseDTO {

    private Long id;
    private String keycloakId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
    private String role;
    private Boolean profileCompleted;
}