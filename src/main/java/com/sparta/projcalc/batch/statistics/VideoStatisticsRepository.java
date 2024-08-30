package com.sparta.projcalc.batch.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * VideoStatisticsRepository는 비디오 통계 데이터를 관리하는 JPA 저장소입니다.
 * 이 저장소는 비디오 통계에 대한 CRUD 작업을 지원합니다.
 */
public interface VideoStatisticsRepository extends JpaRepository<VideoStatistics, Long> {

    /**
     * 주어진 기간 유형과 날짜 범위에 따라 조회 수 상위 5개의 비디오 통계 리스트를 조회합니다.
     *
     * @param periodType 통계 기간 유형 (일간, 주간, 월간)
     * @param startDate 통계 기간의 시작 날짜
     * @param endDate 통계 기간의 종료 날짜
     * @return 조회 수 상위 5개의 비디오 통계 리스트
     */
    @Query("SELECT vs FROM VideoStatistics vs WHERE vs.periodType = :periodType AND vs.startDate BETWEEN :startDate AND :endDate ORDER BY vs.views DESC")
    List<VideoStatistics> findTop5ByPeriodTypeAndStartDateBetweenOrderByViewsDesc(
            @Param("periodType") VideoStatistics.PeriodType periodType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 주어진 기간 유형과 날짜 범위에 따라 시청 시간 상위 5개의 비디오 통계 리스트를 조회합니다.
     *
     * @param periodType 통계 기간 유형 (일간, 주간, 월간)
     * @param startDate 통계 기간의 시작 날짜
     * @param endDate 통계 기간의 종료 날짜
     * @return 시청 시간 상위 5개의 비디오 통계 리스트
     */
    @Query("SELECT vs FROM VideoStatistics vs WHERE vs.periodType = :periodType AND vs.startDate BETWEEN :startDate AND :endDate ORDER BY vs.watchTime DESC")
    List<VideoStatistics> findTop5ByPeriodTypeAndStartDateBetweenOrderByWatchTimeDesc(
            @Param("periodType") VideoStatistics.PeriodType periodType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
