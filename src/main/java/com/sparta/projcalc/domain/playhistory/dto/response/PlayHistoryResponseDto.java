package com.sparta.projcalc.domain.playhistory.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PlayHistoryResponseDto {

    private Long id;
    private Long userId;
    private Long videoId;
    private Long playbackPosition;
    private LocalDateTime watchedAt;
}
