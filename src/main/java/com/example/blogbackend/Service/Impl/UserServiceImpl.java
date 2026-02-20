package com.example.blogbackend.Service.Impl;


import com.example.blogbackend.Domaine.User;
import com.example.blogbackend.Dto.UserProfileRequestDTO;
import com.example.blogbackend.Dto.UserProfileResponseDTO;
import com.example.blogbackend.Repository.UserRepository;
import com.example.blogbackend.Service.Mapper.UserMapper;
import com.example.blogbackend.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfileResponseDTO getMyProfile(Jwt jwt) {
        return userMapper.toDTO(findUser(jwt));
    }

    @Override
    public UserProfileResponseDTO completeProfile(Jwt jwt, UserProfileRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Le body de la requête ne peut pas être vide");
        }

        User user = findUser(jwt);

        if (dto.getBio() != null)       user.setBio(dto.getBio());
        if (dto.getAvatarUrl() != null) user.setAvatarUrl(dto.getAvatarUrl());
        user.setProfileCompleted(true);

        return userMapper.toDTO(userRepository.save(user));
    }

    private User findUser(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur introuvable pour keycloakId: " + keycloakId
                ));
    }
}