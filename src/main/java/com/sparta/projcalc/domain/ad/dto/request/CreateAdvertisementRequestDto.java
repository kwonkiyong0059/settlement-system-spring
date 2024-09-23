package com.sparta.projcalc.domain.ad.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdvertisementRequestDto {
    private String title;
    private String content;
    private String imageUrl;
    private Integer duration;
}
