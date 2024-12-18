package me.toparang.blog.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import me.toparang.blog.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
// JWT를 생성하고 유효한 토큰인지 검증하는 역할
public class TokenProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 method
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .header() // header - type: JWT
                    .add(Header.TYPE, Header.JWT_TYPE)
                    .and()
                .issuer(jwtProperties.getIssuer()) // payload - iss: properties에서 설정한 값
                .issuedAt(now) // payload - iat: 현재 시간
                .expiration(expiry) // payload - exp: 만료 시간
                .subject(user.getEmail()) // payload - sub: 유저의 email
                .claim("id", user.getId()) // payload - claim id: 유저 ID
//                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()) // signature: HS256 방식을 사용해 비밀키 암호화
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
                    .build()// 비밀키로 복호화
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e) { // 복호화 과정 중 에러
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 method
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security
                .core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
    }

    // 토큰 기반으로 User의 ID를 가져오는 method
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
                .build()// 비밀키로 복호화
                .parseSignedClaims(token)
                .getPayload();
    }
}
