package com.sparta.projcalc.Oauth2.kakao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
/**
 * 카카오 소셜 로그인에 필요한 정보를 담고 있는 Bean입니다.
 */
@Getter
@Component
@PropertySource("classpath:application.properties")
public class KakaoLoginProperties {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String codeRequestUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenRequestUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoApiUserInfoRequestUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoAuthBaseUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoApiBaseUri;

}
