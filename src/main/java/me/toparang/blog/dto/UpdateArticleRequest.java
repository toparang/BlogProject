package me.toparang.blog.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class UpdateArticleRequest {
    private String title;
    private String content;
}
