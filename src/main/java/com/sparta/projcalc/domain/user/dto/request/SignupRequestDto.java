package com.sparta.projcalc.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String username;

    @Email(message = "유효하지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+|<>?:{}]).{8,15}$",
            message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자, 숫자, 특수문자로 구성되어야 합니다."
    )
    private String password;

    @NotBlank(message = "역할은 필수 항목입니다.")
    @Pattern(
            regexp = "^(USER|SELLER)$",
            message = "역할은 USER 또는 SELLER만 허용됩니다."
    )
    private String role;
}
