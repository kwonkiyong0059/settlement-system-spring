package com.sparta.projcalc.domain.user.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String token;
    private UserInfo user;
}
