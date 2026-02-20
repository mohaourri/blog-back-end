package com.example.blogbackend.Service;

import com.example.blogbackend.Dto.ArticleRequestDTO;
import com.example.blogbackend.Dto.ArticleResponseDTO;
import org.springframework.data.domain.Page;

public interface ArticleService {

    ArticleResponseDTO createArticle(ArticleRequestDTO dto);

    Page<ArticleResponseDTO> getAllArticles(int page, int size);

    Page<ArticleResponseDTO> searchArticles(String keyword, Long authorId, int page, int size);

    ArticleResponseDTO getArticleByTitle(String title);

    void deleteArticle(Long id);

    ArticleResponseDTO updateArticle(Long id, ArticleRequestDTO dto);
    ArticleResponseDTO getById(Long id);

}
