package com.sparta.projcalc.security.jwt;

import com.sparta.projcalc.common.exception.ErrorCode;
import com.sparta.projcalc.domain.user.entity.User;
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
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class JwtUtil {

    // JWT 관련 상수 정의
    public static final String AUTHORIZATION_ACCESS = "AccessToken";
    public static final String AUTHORIZATION_REFRESH = "RefreshToken";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_TIME = 9 * 60 * 1000L; // 15분
    public static final long REFRESH_TOKEN_TIME = 24 * 60 * 60 * 7 *1000L; // 7일

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    // 암호화 알고리즘 HS256으로 사용할 것임
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

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
     * Access Token 생성
     *
     * @param email 사용자 이메일
     * @param role 사용자 권한
     * @return 생성된 Access Token
     */
    public String createAccessToken(String email, UserRoleEnum role) {

        Date now = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim("role", role)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * Refresh Token 생성
     *
     * @param user 사용자 ID
     * @return 생성된 Refresh Token
     */
    public String createRefreshToken(User user) {

        // 1. 데이터베이스에서 해당 유저의 리프레시 토큰이 존재하는지 확인
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        // 2. 리프레시 토큰이 이미 존재하면 그 값을 반환
        if (existingToken.isPresent()) {
            return existingToken.get().getRefreshToken();
        }
        // 3. 리프레시 토큰이 존재하지 않으면 새로 생성
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_TIME);
        String token = Jwts.builder()
                .claim("id", user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, signatureAlgorithm)
                .compact();

        RefreshToken refreshToken = new RefreshToken(token, user, expiryDate);
        refreshTokenRepository.save(refreshToken);

        return token;
    }

    /**
     * 쿠키에 토큰 추가
     *
     * @param response HttpServletResponse 객체
     * @param token 토큰 문자열 (AccessToken 또는 RefreshToken)
     * @param tokenName 토큰 이름 (AUTHORIZATION_ACCESS 또는 AUTHORIZATION_REFRESH)
     * @param maxAge 쿠키 만료 시간 (초 단위)
     */
    public void addTokenToCookie(HttpServletResponse response, String token, String tokenName, long maxAge) {
        try {

             token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            Cookie cookie = new Cookie(tokenName, token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) maxAge);
            response.addCookie(cookie);

            logger.info("쿠키 추가: 이름 = {}, 값 = {}", tokenName, token);
        } catch (Exception e) {
            logger.error("쿠키에 토큰 추가 실패: {}", e.getMessage());
        }
    }

    /**
     * 액세스 토큰을 쿠키에서 추출합니다.
     *
     * @param request HTTP 요청 객체
     * @return 액세스 토큰 문자열, 또는 쿠키에 액세스 토큰이 없거나 유효하지 않으면 {@code null}
     */
    public String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTHORIZATION_ACCESS.equals(cookie.getName())) {
                    String accessToken = cookie.getValue();
                    try {
                        // URL 디코딩 수행
                        accessToken = URLDecoder.decode(accessToken, "UTF-8");
                        logger.info("디코딩된 액세스 토큰 쿠키 값: {}", accessToken);
                    } catch (Exception e) {
                        logger.error("액세스 토큰 디코딩 실패: {}", e.getMessage());
                    }

                    if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
                        return accessToken.substring(BEARER_PREFIX.length());
                    }
                    logger.error("쿠키의 액세스 토큰이 유효하지 않습니다.");
                    return null;
                }
            }
        }
        logger.error("액세스 토큰 쿠키를 찾을 수 없습니다.");
        return null;
    }

    /**
     * 리프레시 토큰을 쿠키에서 추출합니다.
     *
     * @param request HTTP 요청 객체
     * @return 리프레시 토큰 문자열, 또는 쿠키에 리프레시 토큰이 없거나 유효하지 않으면 {@code null}
     */
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTHORIZATION_REFRESH.equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    logger.info("리프레시 토큰 쿠키 값: {}", refreshToken);
                    if (StringUtils.hasText(refreshToken)) {
                        return refreshToken;
                    }
                    logger.error("쿠키의 리프레시 토큰이 유효하지 않습니다.");
                    return null;
                }
            }
        }
        logger.error("리프레시 토큰 쿠키를 찾을 수 없습니다.");
        return null;
    }

    /**
     * Access Token 유효성 검증
     *
     * @param token 검증할 Access Token 문자열
     * @param request HttpServletRequest 객체
     * @return 유효하면 true, 그렇지 않으면 false
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

    public boolean validateRefreshToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("유효하지 않은 리프레시 토큰: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.INVALID_REFRESH_TOKEN.getCode());
            return false;
        }
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

    public Key getKey() {
        return key;
    }
}
