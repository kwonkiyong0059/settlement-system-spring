package com.sparta.projcalc.domain.video.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleVideoResponseDto {
    private Long id;
    private String title;
    private String url;
}
