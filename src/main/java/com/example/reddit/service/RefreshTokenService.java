package com.example.reddit.service;

import com.example.reddit.exceptions.SpringRedditException;
import com.example.reddit.model.RefreshToken;
import com.example.reddit.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }

    public void validateRefreshToken(String token){
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
    }
}
