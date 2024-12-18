package me.toparang.blog.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//
@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // java class에 property값을 가져와 사용
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
