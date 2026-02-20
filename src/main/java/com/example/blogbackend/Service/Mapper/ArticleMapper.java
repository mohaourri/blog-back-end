package com.example.blogbackend.Service.Mapper;

import com.example.blogbackend.Domaine.Article;
import com.example.blogbackend.Dto.ArticleRequestDTO;
import com.example.blogbackend.Dto.ArticleResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(source = "author.username", target = "authorUsername")
    ArticleResponseDTO toDTO(Article article);

    Article toEntity(ArticleRequestDTO dto);
}
