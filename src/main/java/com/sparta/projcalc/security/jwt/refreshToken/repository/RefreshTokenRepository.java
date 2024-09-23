package com.sparta.projcalc.security.jwt.refreshToken.repository;

import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.security.jwt.refreshToken.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    // User 객체 자체를 받아서 찾는 메서드로 수정
    Optional<RefreshToken> findByUser(User user);

    void deleteByExpiryDateBefore(Date expiryDate);

}
