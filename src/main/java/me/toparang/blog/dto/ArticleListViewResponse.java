package me.toparang.blog.dto;

import lombok.*;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ArticleListViewResponse {
    private final Long id;
    private final String title;
    private final String content;
}
