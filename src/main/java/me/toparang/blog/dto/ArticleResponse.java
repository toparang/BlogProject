package me.toparang.blog.dto;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
}
