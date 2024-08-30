package com.sparta.projcalc.domain.playhistory.repository;

import com.sparta.projcalc.domain.playhistory.entity.PlayHistory;
import com.sparta.projcalc.domain.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
    List<PlayHistory> findByVideoAndWatchedAtBetween(Video video, LocalDateTime startDateTime, LocalDateTime endDateTime);
}

