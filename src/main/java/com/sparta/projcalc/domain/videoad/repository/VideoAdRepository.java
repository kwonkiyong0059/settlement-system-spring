package com.sparta.projcalc.domain.videoad.repository;

import com.sparta.projcalc.domain.videoad.entity.VideoAd;
import com.sparta.projcalc.domain.videoad.entity.VideoAdId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoAdRepository extends JpaRepository<VideoAd, VideoAdId> {
}
