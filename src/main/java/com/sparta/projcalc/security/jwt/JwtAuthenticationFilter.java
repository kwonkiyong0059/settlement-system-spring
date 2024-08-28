package com.sparta.projcalc.domain.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.projcalc.common.exception.EncoreHubException;
import com.sparta.projcalc.common.exception.ErrorCode;
import com.sparta.projcalc.common.response.ResponseMessage;
import com.sparta.projcalc.domain.security.UserDetailsImpl;
import com.sparta.projcalc.domain.user.dto.request.LoginRequestDto;
import com.sparta.projcalc.domain.user.entity.UserRoleEnum;
import com.sparta.projcalc.domain.security.jwt.refreshToken.entity.RefreshToken;
import com.sparta.projcalc.domain.security.jwt.refreshToken.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // 인증
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/member/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();
        Long id = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getId();

        String accessToken = jwtUtil.createAccessToken(username, role);
        String refreshToken = jwtUtil.createRefreshToken(id);

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(JwtUtil.AUTHORIZATION_ACCESS, accessToken);
        response.addHeader(JwtUtil.AUTHORIZATION_REFRESH, refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(new ResponseMessage<>("로그인 성공", null));
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        out.flush();


        refreshTokenRepository.save(new RefreshToken(refreshToken, id));



    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(new EncoreHubException(ErrorCode.FAILED_LOGIN));
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        out.flush();

    }
}