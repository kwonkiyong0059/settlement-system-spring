package com.sparta.projcalc.batch.settlement;

import com.sparta.projcalc.domain.video.entity.Video;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate settlementDate;

    private Long videoSettlementAmount;

    private Long adSettlementAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    public Settlement(LocalDate settlementDate, Long videoSettlementAmount, Long adSettlementAmount, Video video) {
        this.settlementDate = settlementDate;
        this.videoSettlementAmount = videoSettlementAmount;
        this.adSettlementAmount = adSettlementAmount;
        this.video = video;
    }

    public Long getTotalSettlementAmount() {
        return videoSettlementAmount + adSettlementAmount;
    }
}
