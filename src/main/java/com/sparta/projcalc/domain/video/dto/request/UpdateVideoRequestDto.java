package com.sparta.projcalc.domain.video.dto.request;

import com.sparta.projcalc.domain.video.entity.VideoCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVideoRequestDto {

    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    private String description;

    @Pattern(regexp = "^(http|https)://.*$", message = "유효한 URL을 입력해 주세요.")
    @NotBlank(message = "URL은 필수 항목입니다.")
    private String url;

    @NotNull(message = "카테고리는 필수 항목입니다.")
    private VideoCategory category;

    @NotNull(message = "영상 길이는 필수 항목입니다.")
    private Integer duration;
}
