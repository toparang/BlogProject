package me.toparang.blog.config.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.toparang.blog.domain.User;
import me.toparang.blog.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 기반으로 토큰 생성 가능")
    @Test
    void generateToken() {
        User testUser = userRepository.save(User.builder()
                        .email("user.gmail.com")
                        .password("test")
                .build());

        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));
        Long userId = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
                .build()// 비밀키로 복호화
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);

        Assertions.assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken(): 만료된 토큰의 유효성 검증 실패")
    @Test
    void validToken_invalidToken() {
        String token = JwtFactory.builder()
                .expiredAt(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        Assertions.assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 만료된 토큰의 유효성 검증 성공")
    @Test
    void validToken_validToken() {
        String token = JwtFactory.builder()
                .build()
                .createToken(jwtProperties);

        boolean result = tokenProvider.validToken(token);

        Assertions.assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반 인증 정보 불러오기 가능")
    @Test
    void getAuthentication() {
        String email = "user@gmail.com";
        String token = JwtFactory.builder()
                .subject(email)
                .build()
                .createToken(jwtProperties);

        Authentication authentication = tokenProvider.getAuthentication(token);

        Assertions.assertThat(((UserDetails) authentication.getPrincipal()).getUsername())
                .isEqualTo(email);
    }

    @DisplayName("getUserId(): 토큰으로 유저 ID 불러오기 가능")
    @Test
    void getUserId() {
        Long userId = 1L;
        String token = JwtFactory.builder().claims(Map.of("id", userId)).build().createToken(jwtProperties);

        Long userIdByToken = tokenProvider.getUserId(token);

        Assertions.assertThat(userId).isEqualTo(userIdByToken);
    }
}
