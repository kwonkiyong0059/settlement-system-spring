package com.sparta.projcalc.security.jwt.refreshToken.entity;

import com.sparta.projcalc.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    public RefreshToken(String refreshToken, User user, Date expiryDate) {
        this.refreshToken = refreshToken;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    public Long getUserId() {
        return this.user.getId(); // User 객체에서 ID를 추출하는 방법
    }

}
