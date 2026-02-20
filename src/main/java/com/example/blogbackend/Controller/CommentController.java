package com.example.blogbackend.Controller;
import com.example.blogbackend.Const.Consts;
import com.example.blogbackend.Dto.CommentRequestDTO;
import com.example.blogbackend.Dto.CommentResponseDTO;
import com.example.blogbackend.Service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Consts.API_BASE + Consts.API_VERSION + "/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public CommentResponseDTO addComment(
            @Valid
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CommentRequestDTO dto
    ) {
        return commentService.addComment(jwt, dto);
    }


    @GetMapping("/article/{articleId}")
    public Page<CommentResponseDTO> getByArticle(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return commentService.getCommentsByArticle(articleId, page, size);
    }


    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void deleteComment(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(jwt, commentId);
    }
}
