package me.toparang.blog.controller;

import lombok.RequiredArgsConstructor;
import me.toparang.blog.domain.Article;
import me.toparang.blog.dto.AddArticleRequest;
import me.toparang.blog.dto.ArticleResponse;
import me.toparang.blog.dto.UpdateArticleRequest;
import me.toparang.blog.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogApiController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(article -> ArticleResponse.builder()
                        .title(article.getTitle())
                        .content(article.getContent())
                        .build())
                .toList();

        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable("id") Long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok().body(ArticleResponse.builder()
                        .title(article.getTitle())
                        .content(article.getContent())
                        .build());
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
        blogService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable("id") Long id,
                 @RequestBody UpdateArticleRequest request) {
        Article updated = blogService.updateArticle(id, request);

        return ResponseEntity.ok().body(ArticleResponse.builder()
                        .content(updated.getContent())
                        .title(updated.getTitle())
                        .build());
    }
}
