package com.example.blogbackend.Service.Mapper;

import com.example.blogbackend.Domaine.Comment;
import com.example.blogbackend.Dto.CommentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.username", target = "authorUsername")
    @Mapping(source = "article.id",    target = "articleId")
    CommentResponseDTO toDTO(Comment comment);
}