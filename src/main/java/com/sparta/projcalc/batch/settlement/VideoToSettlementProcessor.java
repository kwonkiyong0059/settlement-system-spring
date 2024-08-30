package com.sparta.projcalc.batch.settlement;


import com.sparta.projcalc.domain.ad.entity.Advertisement;
import com.sparta.projcalc.domain.video.entity.Video;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VideoToSettlementProcessor implements ItemProcessor<Video, Settlement> {

    @Override
    public Settlement process(Video video) {
        LocalDate settlementDate = LocalDate.now();
        Long videoSettlementAmount = calculateVideoSettlement(video);
        Long adSettlementAmount = calculateAdSettlement(video);

        return new Settlement(settlementDate, videoSettlementAmount, adSettlementAmount, video);
    }

    private Long calculateVideoSettlement(Video video) {
        Long views = video.getViews();
        double rate;

        if (views < 500000) {
            rate = 1.1;
        } else if (views < 1000000) {
            rate = 1.3;
        } else {
            rate = 1.5;
        }

        return (long) Math.floor(views * rate);
    }

    private Long calculateAdSettlement(Video video) {
        Long adViews = video.getAdvertisements().stream()
                .mapToLong(Advertisement::getViewCount)
                .sum();
        double rate;

        if (adViews < 100000) {
            rate = 10.0;
        } else if (adViews < 500000) {
            rate = 12.0;
        } else if (adViews < 1000000) {
            rate = 15.0;
        } else {
            rate = 20.0;
        }

        return (long) Math.floor(adViews * rate);
    }
}
