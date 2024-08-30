package com.sparta.projcalc.domain.videoad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VideoAdResponseDto {

    private Long videoId;
    private Long adId;
    private LocalDateTime adStartTime; // 광고가 시작되는 시간
}
