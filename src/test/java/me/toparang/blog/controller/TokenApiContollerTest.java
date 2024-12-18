package me.toparang.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.toparang.blog.config.jwt.JwtFactory;
import me.toparang.blog.config.jwt.JwtProperties;
import me.toparang.blog.domain.RefreshToken;
import me.toparang.blog.domain.User;
import me.toparang.blog.dto.CreateAccessTokenRequest;
import me.toparang.blog.repository.RefreshTokenRepository;
import me.toparang.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiContollerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }

    @DisplayName("createNewAccessToken(): 새로운 access token 생성 성공")
    @Test
    void createNewAccessToken() throws Exception{
        final String url = "/api/token";

        User testUser = userRepository.save(User.builder()
                .email("test@gmail.com")
                .password("test")
                .build());

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);

        refreshTokenRepository.save(RefreshToken.builder()
                        .userId(testUser.getId())
                        .refreshToken(refreshToken)
                .build());

        CreateAccessTokenRequest request = CreateAccessTokenRequest.builder()
                .refreshToken(refreshToken)
                .build();
        final String requestBody = objectMapper.writeValueAsString(request);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}
