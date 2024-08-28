package com.sparta.projcalc.domain.security.jwt.refreshToken.repository;

import com.sparta.projcalc.domain.security.jwt.refreshToken.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}