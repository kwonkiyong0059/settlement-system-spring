package com.sparta.projcalc.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.projcalc.common.exception.ProjCalcException;
import com.sparta.projcalc.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 호출됩니다.
 * 사용자가 로그인하지 않았거나, 유효한 인증 토큰이 없는 경우에 401 Unauthorized를 반환합니다.
 * 이 상태 코드는 인증되지 않았음을 나타내며, 사용자에게 인증(로그인)이 필요하다는 것을 알립니다.
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(new ProjCalcException(ErrorCode.UNAUTHORIZED_TO_ACCESS));
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        out.flush();
    }
}