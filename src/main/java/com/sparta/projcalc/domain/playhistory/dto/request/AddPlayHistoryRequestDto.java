package com.sparta.projcalc.domain.playhistory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddPlayHistoryRequestDto {

    private Long userId;
    private Long videoId;
    private Long adId;
    private Long playbackPosition;
}
