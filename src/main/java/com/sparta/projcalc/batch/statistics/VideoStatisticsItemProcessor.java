package com.sparta.projcalc.batch.statistics;

import com.sparta.projcalc.domain.playhistory.entity.PlayHistory;
import com.sparta.projcalc.domain.video.entity.Video;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component("customVideoStatisticsItemProcessor")
public class VideoStatisticsItemProcessor implements ItemProcessor<Video, VideoStatistics> {

    private  String periodType;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        this.periodType = jobParameters.getString("periodType");
    }

    @Override
    public VideoStatistics process(Video video) {
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        switch (periodType) {
            case "DAILY":
                startDate = today;
                endDate = today;
                break;
            case "WEEKLY":
                startDate = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
                endDate = today.with(java.time.temporal.TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
                break;
            case "MONTHLY":
                startDate = today.with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());
                endDate = today.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
                break;
            default:
                throw new IllegalArgumentException("Invalid period type: " + periodType);
        }

        Long views = video.getViews();
        Long watchTime = calculateTotalWatchTime(video, startDate, endDate);

        return VideoStatistics.createStatistics(startDate, endDate, VideoStatistics.PeriodType.valueOf(periodType.toUpperCase()), video, views, watchTime);
    }

    private Long calculateTotalWatchTime(Video video, LocalDate startDate, LocalDate endDate) {
        List<PlayHistory> playHistories = video.getPlayHistories();

        // 날짜 범위에 맞는 시청 기록을 필터링하고 시청 시간을 합산
        return playHistories.stream()
                .filter(playHistory -> isWithinDateRange(playHistory.getWatchedAt(), startDate, endDate))
                .map(PlayHistory::getWatchTime) // 시청 시간 추출
                .reduce(0L, Long::sum); // 전체 시청 시간 합산
    }

    private boolean isWithinDateRange(LocalDateTime watchedAt, LocalDate startDate, LocalDate endDate) {
        LocalDate watchedDate = watchedAt.toLocalDate();
        return !watchedDate.isBefore(startDate) && !watchedDate.isAfter(endDate);
    }
}
