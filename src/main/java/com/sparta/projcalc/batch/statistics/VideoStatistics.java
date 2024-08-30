package com.sparta.projcalc.batch.statistics;

import com.sparta.projcalc.domain.video.entity.Video;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class VideoStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;  // 통계 기간의 시작 날짜
    private LocalDate endDate;    // 통계 기간의 종료 날짜

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;  // 일간, 주간, 월간

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    private Long views;

    private Long watchTime;

    // 불변성을 위한 private 생성자
    private VideoStatistics(LocalDate startDate, LocalDate endDate, PeriodType periodType, Video video, Long views, Long watchTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodType = periodType;
        this.video = video;
        this.views = views;
        this.watchTime = watchTime;
    }

    public enum PeriodType {
        DAILY,
        WEEKLY,
        MONTHLY
    }

    // 정적 생성 메서드
    public static VideoStatistics createStatistics(LocalDate startDate, LocalDate endDate, PeriodType periodType, Video video, Long views, Long watchTime) {
        return new VideoStatistics(startDate, endDate, periodType, video, views, watchTime);
    }
}
