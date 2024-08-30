package com.sparta.projcalc.domain.adview.entity;

import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.domain.ad.entity.Advertisement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ad_view_history")
public class AdViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 사용자 정보와 연관

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Advertisement advertisement;  // 광고 정보와 연관

    @Column(nullable = false)
    private LocalDateTime viewedAt;  // 광고를 본 시점

    // 모든 필드를 초기화하는 생성자
    public AdViewHistory(User user, Advertisement advertisement, LocalDateTime viewedAt) {
        this.user = user;
        this.advertisement = advertisement;
        this.viewedAt = viewedAt;
    }
}
