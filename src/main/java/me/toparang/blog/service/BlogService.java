package me.toparang.blog.service;

import lombok.RequiredArgsConstructor;
import me.toparang.blog.domain.Article;
import me.toparang.blog.dto.AddArticleRequest;
import me.toparang.blog.dto.UpdateArticleRequest;
import me.toparang.blog.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));
    }

    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article updateArticle(Long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));

        Article updated = article.toBuilder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        blogRepository.save(updated);

        return updated;
    }
}
