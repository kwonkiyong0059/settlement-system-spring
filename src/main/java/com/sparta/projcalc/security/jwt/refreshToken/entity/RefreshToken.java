package com.sparta.projcalc.security.jwt.refreshToken.entity;

import com.sparta.projcalc.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public RefreshToken(String refreshToken, User user) {
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public Long getUserId() {
        return this.user.getId(); // User 객체에서 ID를 추출하는 방법
    }

}
