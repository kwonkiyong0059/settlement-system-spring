package com.sparta.projcalc.domain.adview.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdViewHistoryResponseDto {

    private Long id;  // 광고 시청 기록 ID
    private Long userId;  // 사용자 ID
    private Long adId;  // 광고 ID
    private LocalDateTime viewedAt;  // 광고를 본 시각
}
