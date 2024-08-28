package com.sparta.projcalc.domain.security.jwt;

import com.sparta.projcalc.domain.security.jwt.refreshToken.entity.RefreshToken;
import com.sparta.projcalc.common.exception.ErrorCode;
import com.sparta.projcalc.domain.security.jwt.refreshToken.repository.RefreshTokenRepository;
import com.sparta.projcalc.domain.user.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_ACCESS = "AccessToken";
    public static final String AUTHORIZATION_REFRESH = "RefreshToken";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final long ACCESS_TOKEN_TIME = 2 * 60 * 60 * 1000L; // 2시간
    public static final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 1000L;   // 24시간

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Keys.hmacShaKeyFor() : key byte array를 HMAC 알고리즘을 적용한 Key 객체를 생성함.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * accessToken을 만드는 함수.
     *
     * @param email 유저의 이메일
     * @param role  유저 권한
     * @return jwt
     */
    public String createAccessToken(String email, UserRoleEnum role) {

        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, role)
                        .setIssuedAt(date)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * refreshToken을 만드는 함수.
     *
     * @param userId 멤버 id
     * @return refreshToken
     */
    public String createRefreshToken(Long userId) {

        Date date = new Date();

        String token = Jwts.builder()
                .claim("id", userId)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
        RefreshToken refreshToken = new RefreshToken(token, userId);
        refreshTokenRepository.save(refreshToken);
        return token;
    }


    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION_ACCESS);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(7);
        }
        logger.error("Not Found Access Token");
        return null;
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader(AUTHORIZATION_REFRESH);
        if (StringUtils.hasText(refreshToken)) {
            return refreshToken;
        }
        logger.error("Not Found Refresh Token");
        return null;
    }


    /**
     * 토큰을 검증하는 함수
     *
     * @param token
     * @return 검증되면 true, 검증 안되면 false
     */
    public boolean validateAccessToken(String token, HttpServletRequest request) {
        try {
            //헤더에는 JWT를 추출하고, 서명을 검증
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
            //서명검증 실패시
        } catch (SecurityException | MalformedJwtException e) {
            request.setAttribute("exception", ErrorCode.INVALID_ACCESS_TOKEN.getCode());
            //jwt 만료
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.EXPIRATION_ACCESS_TOKEN.getCode());
            // 지원되지않는 형식
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", ErrorCode.NOT_SUPPORTED_ACCESS_TOKEN.getCode());
            //일반적인 오류처리
        } catch (JwtException e) {
            request.setAttribute("exception", ErrorCode.UNKNOWN_ACCESS_TOKEN_ERROR.getCode());
        }

        return false;
    }

    /**
     * JWT에서 사용자 정보 가져오기
     *
     * @param token
     * @return 사용자 정보
     */
    public Claims getUserInfoFormToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
    /**
     * JWT에서 이메일 추출
     *
     * @param token JWT 토큰
     * @return 이메일
     */
    public String getEmailFromToken(String token) {
        Claims claims = getUserInfoFormToken(token);
        return claims.getSubject(); // setSubject로 설정된 이메일을 추출
    }

}
