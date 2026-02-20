package com.example.blogbackend.Service.Impl;

import com.example.blogbackend.Domaine.Article;
import com.example.blogbackend.Domaine.QArticle;
import com.example.blogbackend.Domaine.User;
import com.example.blogbackend.Dto.ArticleRequestDTO;
import com.example.blogbackend.Dto.ArticleResponseDTO;
import com.example.blogbackend.Exception.ArticleNotFoundException;
import com.example.blogbackend.Exception.UserNotFoundException;
import com.example.blogbackend.Service.ArticleService;
import com.example.blogbackend.Service.Mapper.ArticleMapper;
import com.example.blogbackend.Repository.ArticleRepository;
import com.example.blogbackend.Repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;

    @Override
    public ArticleResponseDTO getById(Long id) {

        log.info("Fetching article with id={}", id);

        Article article = articleRepository.findById(id)
                .orElseThrow(() ->
                        new ArticleNotFoundException("Article not found with id: " + id)
                );

        return articleMapper.toDTO(article);
    }

    @Override
    public ArticleResponseDTO createArticle(ArticleRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        log.info("Username from token: {}", username);

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        log.info("Author found: id={}, username={}", author.getId(), author.getUsername());

        Article article = articleMapper.toEntity(dto);
        article.setAuthor(author);

        Article saved = articleRepository.save(article);
        log.info("Article saved: id={}, author={}", saved.getId(), saved.getAuthor().getUsername());

        ArticleResponseDTO response = articleMapper.toDTO(saved);
        log.info("Response authorUsername: {}", response.getAuthorUsername());

        return response;
    }

    @Override
    public Page<ArticleResponseDTO> getAllArticles(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return articleRepository.findAll(pageable)
                .map(articleMapper::toDTO);
    }

    @Override
    public Page<ArticleResponseDTO> searchArticles(String keyword, Long authorId, int page, int size) {

        QArticle article = QArticle.article;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.and(article.title.containsIgnoreCase(keyword));
        }

        if (authorId != null) {
            builder.and(article.author.id.eq(authorId));
        }

        Pageable pageable = PageRequest.of(page, size);

        return articleRepository.findAll(builder, pageable)
                .map(articleMapper::toDTO);
    }

    @Override
    public ArticleResponseDTO getArticleByTitle(String title) {
        Article article = articleRepository.findByTitle(title)
                .orElseThrow(() ->
                        new ArticleNotFoundException("Article not found with title: " + title));
        return articleMapper.toDTO(article);
    }


    @Override
    public void deleteArticle(Long id) {

        if (!articleRepository.existsById(id)) {
            throw new ArticleNotFoundException("Article not found with id: " + id);
        }

        articleRepository.deleteById(id);
    }
    @Override
    public ArticleResponseDTO updateArticle(Long id, ArticleRequestDTO dto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        Article article = articleRepository.findById(id)
                .orElseThrow(() ->
                        new ArticleNotFoundException("Article not found with id: " + id));

        // 🔐 Vérifier que l'utilisateur connecté est le propriétaire
        if (!article.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to update this article");
        }

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());

        return articleMapper.toDTO(articleRepository.save(article));
    }

}