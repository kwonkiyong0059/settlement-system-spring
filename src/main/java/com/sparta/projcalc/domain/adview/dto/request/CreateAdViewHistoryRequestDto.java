package com.sparta.projcalc.domain.adview.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAdViewHistoryRequestDto {

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;  // 사용자 ID

    @NotNull(message = "광고 ID는 필수입니다.")
    private Long adId;  // 광고 ID

    @NotNull(message = "시청 시각은 필수입니다.")
    private LocalDateTime viewedAt;  // 광고를 본 시각
}
