package com.sparta.projcalc.batch.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * 배치 작업을 정기적으로 실행하는 스케줄러입니다.
 */
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private static final Logger logger = Logger.getLogger(ScheduledTasks.class.getName());
    private final BatchJobLauncher batchJobLauncher;

    /**
     * 매일 자정에 비디오 통계 작업을 실행합니다.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailyStatistics() {
        try {
            batchJobLauncher.runVideoStatisticsJob("DAILY");
            logger.info("일간 통계 작업이 성공적으로 실행되었습니다.");
        } catch (Exception e) {
            logger.severe("일간 통계 작업 실행 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 매주 월요일 자정에 비디오 통계 작업을 실행합니다.
     */
    @Scheduled(cron = "0 0 0 * * MON")
    public void generateWeeklyStatistics() {
        try {
            batchJobLauncher.runVideoStatisticsJob("WEEKLY");
            logger.info("주간 통계 작업이 성공적으로 실행되었습니다.");
        } catch (Exception e) {
            logger.severe("주간 통계 작업 실행 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 매월 첫째 날 자정에 비디오 통계 작업을 실행합니다.
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateMonthlyStatistics() {
        try {
            batchJobLauncher.runVideoStatisticsJob("MONTHLY");
            logger.info("월간 통계 작업이 성공적으로 실행되었습니다.");
        } catch (Exception e) {
            logger.severe("월간 통계 작업 실행 중 오류 발생: " + e.getMessage());
        }
    }
}
