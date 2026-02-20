package com.example.blogbackend.Controller;

import com.example.blogbackend.Const.Consts;
import com.example.blogbackend.Dto.ArticleRequestDTO;
import com.example.blogbackend.Dto.ArticleResponseDTO;
import com.example.blogbackend.Service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Consts.API_BASE + Consts.API_VERSION + Consts.ARTICLES)
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    @PreAuthorize("hasRole('AUTHOR')")
    public ArticleResponseDTO create(@RequestBody ArticleRequestDTO dto) {
        return articleService.createArticle(dto);
    }

    @GetMapping
    public Page<ArticleResponseDTO> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return articleService.getAllArticles(page, size);
    }

    @GetMapping("/search")
    public Page<ArticleResponseDTO> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return articleService.searchArticles(keyword, authorId, page, size);
    }


    @GetMapping("/by-title")
    public ArticleResponseDTO getByTitle(@RequestParam String title) {
        return articleService.getArticleByTitle(title);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('AUTHOR')")
    public ArticleResponseDTO update(
            @PathVariable Long id,
            @RequestBody ArticleRequestDTO dto
    ) {
        return articleService.updateArticle(id, dto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> getById(@PathVariable Long id) {

        ArticleResponseDTO response = articleService.getById(id);
        return ResponseEntity.ok(response);
    }


}
