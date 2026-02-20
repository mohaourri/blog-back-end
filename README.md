# Blog Back-End API

API REST sécurisée pour une application de blog, construite avec Spring Boot 4, Keycloak, et MySQL.

---

## Stack technique

| Technologie | Rôle |
|---|---|
| Spring Boot 4 | Framework principal |
| Spring Security + OAuth2 | Sécurité et authentification |
| Keycloak | Identity Provider (SSO) |
| Spring Data JPA + Hibernate | Persistance des données |
| QueryDSL | Recherche dynamique |
| Liquibase | Gestion des migrations BDD |
| MySQL / MariaDB | Base de données |
| MapStruct | Mapping DTO ↔ Entité |
| Lombok | Réduction du boilerplate |
| AOP (Aspect-Oriented Programming) | Logging et monitoring des performances |

---

## Architecture du projet

```
src/main/java/com/example/blogbackend/
├── Aspect/
│   ├── LoggingAspect.java          # Logging automatique (Before, AfterReturning, AfterThrowing)
│   └── PerformanceAspect.java      # Mesure du temps d'exécution (Around)
├── Const/
│   └── Consts.java                 # Constantes des routes API
├── Controller/
│   ├── ArticleController.java      # Endpoints CRUD articles
│   ├── CommentController.java      # Endpoints CRUD commentaires
│   └── UserController.java         # Endpoints profil utilisateur
├── Domaine/
│   ├── Enum/
│   │   └── Role.java               # Enum USER, AUTHOR, ADMIN
│   ├── Article.java                # Entité Article
│   ├── Comment.java                # Entité Comment
│   ├── TracableEntity.java         # Classe mère (id, createdAt, updatedAt)
│   └── User.java                   # Entité User
├── Dto/
│   ├── ArticleRequestDTO.java
│   ├── ArticleResponseDTO.java
│   ├── CommentRequestDTO.java
│   ├── CommentResponseDTO.java
│   ├── UserProfileRequestDTO.java
│   └── UserProfileResponseDTO.java
├── Exception/
│   ├── ArticleNotFoundException.java
│   ├── BadRequestException.java
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java # Gestion centralisée des exceptions
│   └── UserNotFoundException.java
├── Repository/
│   ├── ArticleRepository.java      # Supporte QueryDSL
│   ├── CommentRepository.java
│   └── UserRepository.java
├── Security/
│   ├── JwtAuthConverter.java       # Conversion JWT → Authentication Spring
│   ├── SecurityConfig.java         # Configuration Spring Security
│   └── UserSyncFilter.java         # Synchronisation automatique Keycloak → BDD
└── Service/
    ├── Impl/
    │   ├── ArticleServiceImpl.java
    │   ├── CommentServiceImpl.java
    │   └── UserServiceImpl.java
    ├── Mapper/
    │   └── ArticleMapper.java      # MapStruct
    ├── ArticleService.java
    ├── CommentService.java
    └── UserService.java

src/main/resources/
├── db/changelog/
│   ├── changes/
│   │   ├── 001-create-users.xml
│   │   ├── 002-create-article.xml
│   │   └── 003-create-comment.xml
│   └── db.changelog-master.xml
└── application.properties
```

---

## Endpoints API

### Articles — `/api/v1/articles`

| Méthode | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/articles` | Créer un article |
| `GET` | `/api/v1/articles` | Lister tous les articles (paginé) |
| `GET` | `/api/v1/articles/search` | Rechercher par mot-clé et/ou auteur |
| `GET` | `/api/v1/articles/by-title` | Récupérer un article par titre |
| `PUT` | `/api/v1/articles/{id}` | Modifier un article |
| `DELETE` | `/api/v1/articles/{id}` | Supprimer un article |

#### Paramètres de pagination

| Paramètre | Défaut | Description |
|---|---|---|
| `page` | `0` | Numéro de la page |
| `size` | `5` | Taille de la page |

#### Exemple — Créer un article

```json
POST /api/v1/articles
Authorization: Bearer <token>
Content-Type: application/json

{
    "title": "Mon premier article",
    "content": "Contenu de l'article",
    "authorId": 1
}
```

#### Exemple — Rechercher

```
GET /api/v1/articles/search?keyword=spring&authorId=1&page=0&size=5
Authorization: Bearer <token>
```

---

## Sécurité

L'API est sécurisée avec **Keycloak** en mode Resource Server OAuth2.

- Toutes les routes nécessitent un **Bearer Token JWT** valide
- Le filtre `UserSyncFilter` crée automatiquement l'utilisateur en base de données à la première connexion Keycloak
- Les rôles sont extraits du claim `realm_access.roles` du JWT : `USER`, `AUTHOR`, `ADMIN`

### Configuration Keycloak

```properties
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/blog-realm/protocol/openid-connect/certs
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/blog-realm
```

---

## AOP — Aspect-Oriented Programming

Deux aspects sont actifs sur toutes les méthodes des services :

### LoggingAspect
- **`@Before`** — Log les arguments avant chaque appel
- **`@AfterReturning`** — Log le résultat après succès
- **`@AfterThrowing`** — Log l'exception en cas d'erreur

### PerformanceAspect
- **`@Around`** — Mesure le temps d'exécution
- Affiche un `WARN` si l'exécution dépasse **500 ms**

#### Exemple de logs générés

```
⇒ [CONTROLLER] ArticleController.update()
→ [SERVICE] ArticleServiceImpl.updateArticle() | args: [1, ArticleRequestDTO(...)]
← [SERVICE] ArticleServiceImpl.updateArticle() | résultat: ArticleResponseDTO(...)
⏱ [PERF] ArticleServiceImpl.updateArticle() → 38 ms
```

---

## Base de données

### Migrations Liquibase

| Fichier | Description |
|---|---|
| `001-create-users.xml` | Création de la table `users` |
| `002-create-article.xml` | Création de la table `article` |
| `003-create-comment.xml` | Création de la table `comment` |

### Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
spring.jpa.hibernate.ddl-auto=validate
spring.liquibase.enabled=true
```

---

## Lancer le projet

### Prérequis

- Java 17+
- MySQL / MariaDB
- Keycloak (démarré sur le port `8080`)
- Maven

### Étapes

```bash
# 1. Cloner le projet
git clone <url-du-repo>

# 2. Configurer application.properties
# - URL BDD
# - Credentials MySQL
# - URL Keycloak

# 3. Lancer
mvn spring-boot:run
```

L'API sera disponible sur `http://localhost:8089`.

---

## Gestion des erreurs

Toutes les erreurs sont gérées par `GlobalExceptionHandler` et retournent un format uniforme :

```json
{
    "status": 404,
    "error": "Not Found",
    "message": "Article not found with id: 99",
    "path": "/api/v1/articles/99",
    "timestamp": "2026-02-18T17:33:32.824"
}
```

| Exception | Code HTTP |
|---|---|
| `ArticleNotFoundException` | `404 Not Found` |
| `UserNotFoundException` | `404 Not Found` |
| `BadRequestException` | `400 Bad Request` |
| Autres | `500 Internal Server Error` |
