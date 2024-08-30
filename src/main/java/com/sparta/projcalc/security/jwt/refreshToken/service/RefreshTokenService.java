package com.sparta.projcalc.security.jwt.refreshToken.service;

import com.sparta.projcalc.common.exception.ProjCalcException;
import com.sparta.projcalc.common.exception.ErrorCode;
import com.sparta.projcalc.security.jwt.JwtUtil;
import com.sparta.projcalc.security.jwt.refreshToken.entity.RefreshToken;
import com.sparta.projcalc.security.jwt.refreshToken.repository.RefreshTokenRepository;
import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    /**
     * refresh token 이 유효하고 동일한지 확인한다.
     * 만약 올바른 토큰이라면 access token 을 발급하고,
     * 그렇지 않다면 refresh 토큰 삭제 후 다시 로그인하도록 응답한다.
     * @param refreshToken 확인할 refresh token
     * @return 유효한 refresh token 이라면 새로운 access token 반환
     */
    @Transactional
    public String getAccess(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new ProjCalcException(ErrorCode.EXPIRATION_REFRESH_TOKEN));
        if (!token.getRefreshToken().equals(refreshToken)) {
            refreshTokenRepository.delete(token);
            throw new ProjCalcException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new ProjCalcException(ErrorCode.NOT_FOUND_USER));
        return jwtUtil.createAccessToken(user.getEmail(), user.getRole());
    }
}