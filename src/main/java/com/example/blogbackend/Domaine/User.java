package com.example.blogbackend.Domaine;

import com.example.blogbackend.Domaine.Enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User extends TracableEntity {

    @Column(unique = true, nullable = false)
    private String keycloakId;   // "sub" du JWT → clé de synchronisation

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;     // non utilisé avec Keycloak, gardé pour compatibilité

    private String firstName;    // given_name

    private String lastName;     // family_name

    private String bio;          // à compléter via l'endpoint /profile

    private String avatarUrl;    // à compléter via l'endpoint /profile

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Builder.Default
    private Boolean profileCompleted = false;  // false tant que /profile non rempli

    @OneToMany(mappedBy = "author")
    private List<Article> articles;
}