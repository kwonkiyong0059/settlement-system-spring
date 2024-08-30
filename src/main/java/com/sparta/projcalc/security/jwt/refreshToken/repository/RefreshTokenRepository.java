package com.sparta.projcalc.security.jwt.refreshToken.repository;

import com.sparta.projcalc.security.jwt.refreshToken.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
