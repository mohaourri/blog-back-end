package com.example.blogbackend.Domaine;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Comment extends TracableEntity {
    @Column(length = 5000)
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne
    private Article article;

    @ManyToOne
    private User user;
}
