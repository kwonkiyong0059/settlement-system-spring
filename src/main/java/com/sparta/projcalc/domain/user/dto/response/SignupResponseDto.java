package com.sparta.projcalc.domain.user.dto.response;

import com.sparta.projcalc.domain.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SignupResponseDto {
    private Long id;
    private String username;
    private String email;
    private UserRoleEnum role;
    private LocalDateTime createdAt;
}
