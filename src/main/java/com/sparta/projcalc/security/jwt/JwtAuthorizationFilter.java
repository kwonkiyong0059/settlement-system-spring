package com.sparta.projcalc.security.jwt;

import com.sparta.projcalc.common.exception.ErrorCode;
import com.sparta.projcalc.common.exception.ProjCalcException;
import com.sparta.projcalc.security.UserDetailsImpl;
import com.sparta.projcalc.security.UserDetailsServiceImpl;
import com.sparta.projcalc.security.jwt.refreshToken.entity.RefreshToken;
import com.sparta.projcalc.security.jwt.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getAccessTokenFromCookies(req);
        String refreshToken = jwtUtil.getRefreshTokenFromCookies(req);

        if (StringUtils.hasText(accessToken) && jwtUtil.validateAccessToken(accessToken, req)) {
            log.info("Access Token is valid");
            Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
            setAuthentication(claims.getSubject());
        } else if (StringUtils.hasText(refreshToken) && jwtUtil.validateAccessToken(refreshToken, req)) {
            log.info("Refresh Token is valid");
            handleRefreshToken(req, res, refreshToken);
        } else {
            log.error("Invalid tokens");
        }

        filterChain.doFilter(req, res);
    }

    private void handleRefreshToken(HttpServletRequest req, HttpServletResponse res, String refreshToken) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new ProjCalcException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));

        Long storedUserId = storedRefreshToken.getUserId();
        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        Long tokenUserId = claims.get("id", Long.class);

        if (tokenUserId.equals(storedUserId)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserById(tokenUserId);
            String newAccessToken = jwtUtil.createAccessToken(userDetails.getUsername(), userDetails.getUser().getRole());
            res.addHeader(JwtUtil.AUTHORIZATION_ACCESS, newAccessToken);
            setAuthentication(userDetails.getUsername());
        } else {
            log.error("Invalid Refresh Token user ID");
        }
    }

    private void setAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
