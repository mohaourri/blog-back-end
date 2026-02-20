package com.example.blogbackend.Service;

import com.example.blogbackend.Dto.UserProfileRequestDTO;
import com.example.blogbackend.Dto.UserProfileResponseDTO;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {


    UserProfileResponseDTO getMyProfile(Jwt jwt);


    UserProfileResponseDTO completeProfile(Jwt jwt, UserProfileRequestDTO dto);
}