package com.example.blogbackend.Security;

import com.example.blogbackend.Domaine.User;
import com.example.blogbackend.Domaine.Enum.Role;
import com.example.blogbackend.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSyncFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // ✅ Sync AVANT la chaîne — BearerTokenAuthenticationFilter
        //    (placé avant ce filtre) a déjà rempli le SecurityContext
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                syncUserFromJwt(jwtAuth.getToken());
            }
        } catch (Exception e) {
            log.error("Erreur lors de la synchronisation de l'utilisateur Keycloak : {}", e.getMessage(), e);
        }

        // ✅ doFilter EN DERNIER — le service trouvera l'user déjà en BDD
        filterChain.doFilter(request, response);
    }

    private void syncUserFromJwt(Jwt jwt) {
        String keycloakId = jwt.getSubject();

        if (keycloakId == null || keycloakId.isBlank()) {
            log.warn("JWT sans 'sub', synchronisation ignorée");
            return;
        }

        String username  = jwt.getClaimAsString("preferred_username");
        String email     = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName  = jwt.getClaimAsString("family_name");
        Role role        = extractRole(jwt);

        userRepository.findByKeycloakId(keycloakId).orElseGet(() -> {
            log.info("Nouvel utilisateur Keycloak détecté → insertion en BDD : {}", username);
            User newUser = User.builder()
                    .keycloakId(keycloakId)
                    .username(username)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .role(role)
                    .profileCompleted(false)
                    .build();
            return userRepository.save(newUser);
        });
    }

    private Role extractRole(Jwt jwt) {
        try {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null) {
                Collection<String> roles = (Collection<String>) realmAccess.get("roles");
                if (roles != null) {
                    if (roles.contains("AUTHOR")) return Role.AUTHOR;
                    if (roles.contains("ADMIN"))  return Role.ADMIN;
                }
            }
        } catch (Exception e) {
            log.warn("Impossible d'extraire le rôle depuis le JWT : {}", e.getMessage());
        }
        return Role.USER;
    }
}