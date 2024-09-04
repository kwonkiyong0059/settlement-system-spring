package com.sparta.projcalc.security.jwt.refreshToken.service;

import com.sparta.projcalc.security.jwt.refreshToken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void removeExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(new Date());
    }
}

