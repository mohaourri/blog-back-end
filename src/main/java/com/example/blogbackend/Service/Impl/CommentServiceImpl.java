package com.example.blogbackend.Service.Impl;

import com.example.blogbackend.Domaine.Article;
import com.example.blogbackend.Domaine.Comment;
import com.example.blogbackend.Domaine.User;
import com.example.blogbackend.Dto.CommentRequestDTO;
import com.example.blogbackend.Dto.CommentResponseDTO;
import com.example.blogbackend.Exception.ArticleNotFoundException;
import com.example.blogbackend.Exception.BadRequestException;
import com.example.blogbackend.Exception.UserNotFoundException;
import com.example.blogbackend.Repository.ArticleRepository;
import com.example.blogbackend.Repository.CommentRepository;
import com.example.blogbackend.Repository.UserRepository;
import com.example.blogbackend.Service.CommentService;
import com.example.blogbackend.Service.Mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository  commentRepository;
    private final ArticleRepository  articleRepository;
    private final UserRepository     userRepository;
    private final CommentMapper      commentMapper;

    @Override
    public CommentResponseDTO addComment(Jwt jwt, CommentRequestDTO dto) {

        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new BadRequestException("Le contenu du commentaire ne peut pas être vide");
        }

        User user = userRepository.findByKeycloakId(jwt.getSubject())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur introuvable"));

        Article article = articleRepository.findById(dto.getArticleId())
                .orElseThrow(() -> new ArticleNotFoundException(
                        "Article introuvable avec l'id: " + dto.getArticleId()
                ));

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .build();

        return commentMapper.toDTO(commentRepository.save(comment));
    }

    @Override
    public Page<CommentResponseDTO> getCommentsByArticle(Long articleId, int page, int size) {

        if (!articleRepository.existsById(articleId)) {
            throw new ArticleNotFoundException("Article introuvable avec l'id: " + articleId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return commentRepository.findByArticleId(articleId, pageable)
                .map(commentMapper::toDTO);
    }

    @Override
    public void deleteComment(Jwt jwt, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ArticleNotFoundException(
                        "Commentaire introuvable avec l'id: " + commentId
                ));

        // Seul l'auteur du commentaire peut le supprimer
        String keycloakId = jwt.getSubject();
        if (!comment.getUser().getKeycloakId().equals(keycloakId)) {
            throw new BadRequestException("Vous ne pouvez pas supprimer le commentaire d'un autre utilisateur");
        }

        commentRepository.deleteById(commentId);
    }
}