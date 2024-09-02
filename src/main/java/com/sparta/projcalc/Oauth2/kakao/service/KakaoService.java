package com.sparta.projcalc.Oauth2.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.projcalc.Oauth2.kakao.KakaoLoginProperties;
import com.sparta.projcalc.Oauth2.kakao.dto.KakaoUserInfoDto;
import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.domain.user.entity.UserRoleEnum;
import com.sparta.projcalc.domain.user.repository.UserRepository;
import com.sparta.projcalc.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final KakaoLoginProperties kakaoLoginProperties;

    /**
     * 카카오 로그인 후 액세스 토큰을 생성하고 반환합니다.
     * @param code 카카오 인증 서버로부터 받은 인증 코드
     * @return JWT 액세스,리프레시 토큰
     * @throws JsonProcessingException JSON 처리 예외
     */
    public Map<String, String> kakaoLogin(String code) throws JsonProcessingException {
        String accessToken = getToken(code);
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        User user = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 액세스 토큰 및 리프레시 토큰 생성
        String jwtAccessToken = jwtUtil.createAccessToken(user.getEmail(), user.getRole());
        String jwtRefreshToken = jwtUtil.createRefreshToken(user);

        // 토큰을 맵에 담아서 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtAccessToken);
        tokens.put("refreshToken", jwtRefreshToken);

        return tokens;
    }


    /**
     * 카카오 인증 서버로부터 액세스 토큰을 가져옵니다.
     * @param code 카카오 인증 코드
     * @return 액세스 토큰
     * @throws JsonProcessingException JSON 처리 예외
     */
    private String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoLoginProperties.getKakaoClientId());
        body.add("redirect_uri", kakaoLoginProperties.getKakaoRedirectUrl());
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(requestEntity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to get access token: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (Exception e) {
            log.error("Error while getting access token", e);
            throw new RuntimeException("Failed to get access token", e);
        }

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    /**
     * 액세스 토큰을 사용하여 카카오 사용자 정보를 가져옵니다.
     * @param accessToken 카카오 액세스 토큰
     * @return 카카오 사용자 정보 DTO
     * @throws JsonProcessingException JSON 처리 예외
     */
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(requestEntity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to get user info: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (Exception e) {
            log.error("Error while getting user info", e);
            throw new RuntimeException("Failed to get user info", e);
        }

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return new KakaoUserInfoDto(id, nickname, email);
    }

    /**
     * 카카오 사용자 정보를 기반으로 사용자를 등록하거나 업데이트합니다.
     * @param kakaoUserInfo 카카오 사용자 정보 DTO
     * @return 등록되거나 업데이트된 사용자 객체
     */
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        Long kakaoId = kakaoUserInfo.getId();
        Optional<User> optionalUser = userRepository.findByKakaoId(kakaoId);
        User user = optionalUser.orElse(null);

        if (user == null) {
            String kakaoEmail = kakaoUserInfo.getEmail();
            Optional<User> sameEmailUserOpt = userRepository.findByEmail(kakaoEmail);
            if (sameEmailUserOpt.isPresent()) {
                user = sameEmailUserOpt.get();
                user = user.kakaoIdUpdate(kakaoId);
            } else {
                String password = UUID.randomUUID().toString(); // 비밀번호는 임시로 UUID를 사용하지만, 실제 서비스에서는 다른 방식 권장
                String encodedPassword = passwordEncoder.encode(password);
                user = new User(kakaoUserInfo.getNickname(), encodedPassword, kakaoEmail, UserRoleEnum.USER, kakaoId);
            }

            userRepository.save(user);
        }

        return user;
    }
}
