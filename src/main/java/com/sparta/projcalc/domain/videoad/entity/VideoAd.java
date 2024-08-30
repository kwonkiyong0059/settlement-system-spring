package com.sparta.projcalc.domain.videoad.entity;

import com.sparta.projcalc.domain.ad.entity.Advertisement;
import com.sparta.projcalc.domain.video.entity.Video;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "video_ad")
public class VideoAd {

    @EmbeddedId
    private VideoAdId id;

    @MapsId("videoId")
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @MapsId("adId")
    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Advertisement advertisement;

    @Column(name = "ad_start_time")
    private LocalDateTime adStartTime;

    // Constructor for creating a new VideoAd
    public VideoAd(Video video, Advertisement advertisement, LocalDateTime adStartTime) {
        this.video = video;
        this.advertisement = advertisement;
        this.id = new VideoAdId(video.getId(), advertisement.getId());
        this.adStartTime = adStartTime;
    }
}
