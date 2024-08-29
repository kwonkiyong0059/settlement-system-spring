package com.sparta.projcalc.domain.video.dto.request;

import com.sparta.projcalc.domain.video.entity.VideoCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVideoRequestDto {

    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    @NotBlank(message = "설명은 필수 항목입니다.")
    private String description;

    @NotBlank(message = "URL은 필수 항목입니다.")
    private String url;

    @NotNull(message = "카테고리는 필수 항목입니다.")
    private VideoCategory category;
}
