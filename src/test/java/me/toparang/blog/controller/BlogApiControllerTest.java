package me.toparang.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.toparang.blog.domain.Article;
import me.toparang.blog.dto.AddArticleRequest;
import me.toparang.blog.dto.UpdateArticleRequest;
import me.toparang.blog.repository.BlogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void setMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가 성공")
    @Test
    public void addArticle() throws Exception{
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest request = AddArticleRequest.builder()
                .title(title)
                .content(content)
                .build();

        final String requestBody = objectMapper.writeValueAsString(request);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        Assertions.assertThat(articles.size()).isEqualTo(1);
        Assertions.assertThat(articles.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회 성공")
    @Test
    public void findAllArticles() throws Exception {
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content));
    }

    @DisplayName("findArticle: 블로그 글 조회 성공")
    @Test
    public void findArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article saved = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final ResultActions result = mockMvc.perform(get(url, saved.getId()));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제 성공")
    @Test
    public void deleteArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article saved = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final ResultActions result = mockMvc.perform(delete(url, saved.getId()));

        result.andExpect(status().isOk());

        List<Article> articles = blogRepository.findAll();

        Assertions.assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 블로그 수정 성공")
    @Test
    public void updateArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article saved = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        final String ntitle = "ntitle";
        final String ncontent = "ncontent";
        UpdateArticleRequest request = UpdateArticleRequest.builder()
                .title(ntitle)
                .content(ncontent)
                .build();

        final ResultActions result = mockMvc.perform(put(url, saved.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk());

        Article article = blogRepository.findById(saved.getId()).get();

        Assertions.assertThat(article.getTitle()).isEqualTo(ntitle);
        Assertions.assertThat(article.getContent()).isEqualTo(ncontent);
    }
}