package com.sparta.projcalc.domain.user.dto.response;

import com.sparta.projcalc.domain.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String role;

    public UserInfo(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
