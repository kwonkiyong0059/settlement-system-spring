package com.sparta.projcalc.Oauth2.kakao.controller;

import com.sparta.projcalc.Oauth2.kakao.service.KakaoService;
import com.sparta.projcalc.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/oauth/kakao/callback")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        // 카카오 로그인 후 액세스 및 리프레시 토큰 발급
        Map<String, String> tokens = kakaoService.kakaoLogin(code);

        // 액세스 토큰 쿠키에 저장
        Cookie accessTokenCookie = new Cookie(JwtUtil.AUTHORIZATION_ACCESS, tokens.get("accessToken"));
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        response.addCookie(accessTokenCookie);

        // 리프레시 토큰 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie(JwtUtil.AUTHORIZATION_REFRESH, tokens.get("refreshToken"));
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);

        // 로그인 성공 후 홈 페이지로 리디렉션
        response.sendRedirect("/home");
    }

}