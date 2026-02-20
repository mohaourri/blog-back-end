package com.example.blogbackend.Service.Mapper;

import com.example.blogbackend.Domaine.User;
import com.example.blogbackend.Dto.UserProfileResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role", target = "role")
    UserProfileResponseDTO toDTO(User user);
}