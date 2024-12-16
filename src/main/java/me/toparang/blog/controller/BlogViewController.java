package me.toparang.blog.controller;

import lombok.RequiredArgsConstructor;
import me.toparang.blog.domain.Article;
import me.toparang.blog.dto.ArticleListViewResponse;
import me.toparang.blog.dto.ArticleViewResponse;
import me.toparang.blog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogViewController {
    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(article -> ArticleListViewResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .build())
                .toList();

        model.addAttribute("articles", articles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", ArticleViewResponse.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .content(article.getContent())
                        .createdAt(article.getCreatedAt())
                .build());

        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false, name = "id") Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", ArticleViewResponse.builder().build());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", ArticleViewResponse.toDto(article));
        }

        return "newArticle";
    }
}
