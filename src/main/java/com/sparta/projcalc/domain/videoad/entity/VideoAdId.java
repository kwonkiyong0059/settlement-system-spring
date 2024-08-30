package com.sparta.projcalc.domain.videoad.entity;

import java.io.Serializable;
import java.util.Objects;

public class VideoAdId implements Serializable {

    private Long videoId;
    private Long adId;

    public VideoAdId() {}

    public VideoAdId(Long videoId, Long adId) {
        this.videoId = videoId;
        this.adId = adId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoAdId that = (VideoAdId) o;
        return Objects.equals(videoId, that.videoId) && Objects.equals(adId, that.adId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, adId);
    }
}
