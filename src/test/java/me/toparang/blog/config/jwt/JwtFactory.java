package me.toparang.blog.config.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Getter
@Builder
public class JwtFactory {
    @Builder.Default
    private String subject = "test@email.com";
    @Builder.Default
    private Date issuedAt = new Date();
    @Builder.Default
    private Date expiredAt = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
    @Builder.Default
    private Map<String, Object> claims = Collections.emptyMap();

    public String createToken(JwtProperties jwtProperties) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .header() // header - type: JWT
                    .add(Header.TYPE, Header.JWT_TYPE)
                    .and()
                .issuer(jwtProperties.getIssuer())
                .claims(claims)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()), Jwts.SIG.HS256)
                .compact();
    }
}
