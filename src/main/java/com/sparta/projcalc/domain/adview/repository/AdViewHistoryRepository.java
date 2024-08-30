package com.sparta.projcalc.domain.adview.repository;

import com.sparta.projcalc.domain.adview.entity.AdViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdViewHistoryRepository extends JpaRepository<AdViewHistory, Long> {
    // 광고 ID로 광고 시청 기록 조회
    List<AdViewHistory> findByAdvertisementId(Long adId);
}
