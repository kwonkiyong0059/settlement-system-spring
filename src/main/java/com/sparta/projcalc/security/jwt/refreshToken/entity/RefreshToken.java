package com.sparta.projcalc.security.jwt.refreshToken.entity;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RequiredArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 86400)
public class RefreshToken {

    @Id
    private final String refreshToken;

    private final Long userId;
}
