package com.sparta.projcalc.domain.playhistory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlayHistoryRequestDto {

    private Long playbackPosition;
}
