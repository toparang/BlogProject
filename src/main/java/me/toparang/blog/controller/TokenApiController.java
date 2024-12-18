package me.toparang.blog.controller;

import lombok.RequiredArgsConstructor;
import me.toparang.blog.dto.CreateAccessTokenRequest;
import me.toparang.blog.dto.CreateAccessTokenResponse;
import me.toparang.blog.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(
            @RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreateAccessTokenResponse.builder()
                        .accessToken(newAccessToken)
                        .build());
    }
}
