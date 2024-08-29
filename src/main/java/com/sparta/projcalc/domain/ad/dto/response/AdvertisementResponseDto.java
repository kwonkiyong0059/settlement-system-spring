package com.sparta.projcalc.domain.ad.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdvertisementResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
