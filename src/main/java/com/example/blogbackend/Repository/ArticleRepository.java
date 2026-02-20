package com.example.blogbackend.Repository;

import com.example.blogbackend.Domaine.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> , QuerydslPredicateExecutor<Article> {
    Optional<Article> findByTitle(String title);
}
