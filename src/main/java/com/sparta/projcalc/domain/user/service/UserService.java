package com.sparta.projcalc.domain.user.service;

import com.sparta.projcalc.domain.user.dto.request.SignupRequestDto;
import com.sparta.projcalc.domain.user.dto.response.SignupResponseDto;
import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.domain.user.entity.UserRoleEnum;
import com.sparta.projcalc.domain.user.exception.UserAlreadyExistsException;
import com.sparta.projcalc.domain.user.exception.UserNotFoundException;
import com.sparta.projcalc.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupDto) {
        // 이메일로 사용자 존재 여부 확인
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("이메일이 이미 존재합니다.");
        }

        // 역할 설정
        UserRoleEnum role = UserRoleEnum.valueOf(signupDto.getRole().toUpperCase());

        // 사용자 객체 생성 및 저장
        User user = User.builder()
                .username(signupDto.getUsername())
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword())) // 비밀번호 암호화
                .role(role)
                .build();

        userRepository.save(user);

        // 응답 DTO 반환
        return SignupResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole()) // UserRoleEnum 타입 유지
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }
}
