package com.sparta.projcalc.domain.video.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VideoResponseDto {
    private Long id;
    private String title;
    private String description;
    private String url;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
