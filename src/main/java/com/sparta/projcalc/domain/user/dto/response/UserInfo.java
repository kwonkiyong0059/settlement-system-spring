package com.sparta.projcalc.domain.user.dto;

import lombok.Getter;

@Getter
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String role;
}
