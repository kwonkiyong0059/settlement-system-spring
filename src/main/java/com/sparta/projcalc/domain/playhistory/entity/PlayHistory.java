package com.sparta.projcalc.domain.playhistory.entity;

import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.domain.video.entity.Video;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "playback_history")
public class PlayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "videoid", nullable = false)
    private Video video;

    @Column(nullable = false)
    private Long playbackPosition;

    @Column(nullable = false)
    private LocalDateTime watchedAt; // 마지막으로 시청한 시각

    private LocalDateTime viewTime; // 시청 시작 시각

    private Long watchTime; // 시청한 총 시간 (초 단위)

    // 모든 필드를 초기화하는 생성자
    public PlayHistory(User user, Video video, Long playbackPosition, LocalDateTime watchedAt, LocalDateTime viewTime) {
        this.user = user;
        this.video = video;
        this.playbackPosition = playbackPosition;
        this.watchedAt = watchedAt;
        this.viewTime = viewTime;
        this.watchTime = 0L; // 초기 시청 시간은 0으로 설정
    }

    // 재생 위치와 시청 시간을 업데이트하기 위한 메서드
    public void updatePlaybackPosition(Long playbackPosition) {
        this.playbackPosition = playbackPosition;
        this.watchedAt = LocalDateTime.now();
        updateWatchTime(); // 시청 시간을 업데이트
    }

    // 시청 시간을 업데이트하는 메서드
    private void updateWatchTime() {
        if (this.viewTime != null) {
            this.watchTime = java.time.Duration.between(this.viewTime, this.watchedAt).getSeconds();
        }
    }
}
