package com.sparta.projcalc.security.jwt;

import com.sparta.projcalc.common.exception.ErrorCode;
import com.sparta.projcalc.domain.user.entity.UserRoleEnum;
import com.sparta.projcalc.security.jwt.refreshToken.entity.RefreshToken;
import com.sparta.projcalc.security.jwt.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_ACCESS = "AccessToken";
    public static final String AUTHORIZATION_REFRESH = "RefreshToken";
    public static final long ACCESS_TOKEN_TIME = 2 * 60 * 60; // 2시간 (초 단위)
    public static final long REFRESH_TOKEN_TIME = 24 * 60 * 60; // 24시간 (초 단위)

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * JWT 유틸리티 초기화 메서드.
     * @PostConstruct 애너테이션을 사용하여 빈이 초기화될 때 호출됩니다.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token을 생성하는 메서드.
     * @param email 유저의 이메일
     * @param role 유저의 역할
     * @return 생성된 Access Token 문자열
     */
    public String createAccessToken(String email, UserRoleEnum role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME * 1000)) // ms로 변환
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * Refresh Token을 생성하는 메서드.
     * @param userId 유저 ID
     * @return 생성된 Refresh Token 문자열
     */
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        String token = Jwts.builder()
                .claim("id", userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME * 1000)) // ms로 변환
                .signWith(key, signatureAlgorithm)
                .compact();

        RefreshToken refreshToken = new RefreshToken(token, userId);
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    /**
     * 쿠키에 토큰을 추가하는 메서드.
     * @param response HttpServletResponse 객체
     * @param token 토큰 문자열 (AccessToken 또는 RefreshToken)
     * @param tokenName 토큰 이름 (AUTHORIZATION_ACCESS 또는 AUTHORIZATION_REFRESH)
     * @param maxAge 쿠키의 만료 시간 (초 단위)
     */
    public void addTokenToCookie(HttpServletResponse response, String token, String tokenName, long maxAge) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAge);
        response.addCookie(cookie);
    }

    /**
     * 쿠키에서 토큰을 추출하는 메서드.
     * @param request HttpServletRequest 객체
     * @param tokenName 토큰 이름 (AUTHORIZATION_ACCESS 또는 AUTHORIZATION_REFRESH)
     * @return 토큰 문자열, 찾을 수 없는 경우 null
     */
    public String getTokenFromCookies(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        logger.error("쿠키에서 토큰을 찾을 수 없습니다: {}", tokenName);
        return null;
    }

    /**
     * 쿠키에서 Access Token을 추출하는 메서드.
     * @param request HttpServletRequest 객체
     * @return Access Token 문자열, 찾을 수 없는 경우 null
     */
    public String getAccessTokenFromCookies(HttpServletRequest request) {
        return getTokenFromCookies(request, AUTHORIZATION_ACCESS);
    }

    /**
     * 쿠키에서 Refresh Token을 추출하는 메서드.
     * @param request HttpServletRequest 객체
     * @return Refresh Token 문자열, 찾을 수 없는 경우 null
     */
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        return getTokenFromCookies(request, AUTHORIZATION_REFRESH);
    }

    /**
     * Access Token의 유효성을 검증하는 메서드.
     * @param token 검증할 Access Token 문자열
     * @param request HttpServletRequest 객체
     * @return 유효한 경우 true, 그렇지 않은 경우 false
     */
    public boolean validateAccessToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("유효하지 않은 액세스 토큰: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.INVALID_ACCESS_TOKEN.getCode());
        } catch (ExpiredJwtException e) {
            logger.error("액세스 토큰이 만료되었습니다: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.EXPIRATION_ACCESS_TOKEN.getCode());
        } catch (UnsupportedJwtException e) {
            logger.error("지원하지 않는 액세스 토큰: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.NOT_SUPPORTED_ACCESS_TOKEN.getCode());
        } catch (JwtException e) {
            logger.error("알 수 없는 액세스 토큰 오류: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.UNKNOWN_ACCESS_TOKEN_ERROR.getCode());
        }
        return false;
    }

    /**
     * JWT에서 사용자 정보를 추출하는 메서드.
     * @param token JWT 토큰 문자열
     * @return JWT에서 추출한 Claims 객체
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * JWT에서 이메일을 추출하는 메서드.
     * @param token JWT 토큰 문자열
     * @return 이메일
     */
    public String getEmailFromToken(String token) {
        Claims claims = getUserInfoFromToken(token);
        return claims.getSubject();
    }
}
