package me.toparang.blog.dto;

import lombok.*;
import me.toparang.blog.domain.Article;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public static Object toDto(Article article) {
        return ArticleViewResponse.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .id(article.getId())
                .author(article.getAuthor())
                .createdAt(article.getCreatedAt())
                .build();
    }
}
