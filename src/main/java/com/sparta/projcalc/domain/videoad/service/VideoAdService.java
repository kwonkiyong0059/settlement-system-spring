package com.sparta.projcalc.domain.videoad.service;

import com.sparta.projcalc.domain.ad.entity.Advertisement;
import com.sparta.projcalc.domain.ad.exception.AdvertisementNotFoundException;
import com.sparta.projcalc.domain.ad.repository.AdvertisementRepository;
import com.sparta.projcalc.domain.video.entity.Video;
import com.sparta.projcalc.domain.video.exception.VideoNotFoundException;
import com.sparta.projcalc.domain.video.repository.VideoRepository;
import com.sparta.projcalc.domain.videoad.dto.request.CreateVideoAdRequestDto;
import com.sparta.projcalc.domain.videoad.dto.response.VideoAdResponseDto;
import com.sparta.projcalc.domain.videoad.entity.VideoAd;
import com.sparta.projcalc.domain.videoad.entity.VideoAdId;
import com.sparta.projcalc.domain.videoad.repository.VideoAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoAdService {

    private final VideoAdRepository videoAdRepository;
    private final VideoRepository videoRepository;
    private final AdvertisementRepository advertisementRepository;

    // 광고를 비디오에 추가
    public VideoAdResponseDto addVideoAd(CreateVideoAdRequestDto requestDto) {
        Video video = videoRepository.findById(requestDto.getVideoId())
                .orElseThrow(() -> new VideoNotFoundException("비디오를 찾을 수 없습니다."));
        Advertisement advertisement = advertisementRepository.findById(requestDto.getAdId())
                .orElseThrow(() -> new AdvertisementNotFoundException("광고를 찾을 수 없습니다."));

        VideoAd videoAd = new VideoAd(video, advertisement, requestDto.getAdStartTime());
        VideoAd created = videoAdRepository.save(videoAd);

        return new VideoAdResponseDto(
                created.getVideo().getId(),
                created.getAdvertisement().getId(),
                created.getAdStartTime()
        );
    }

    // 모든 비디오-광고 관계 조회
    public List<VideoAdResponseDto> getAllVideoAds() {
        return videoAdRepository.findAll().stream()
                .map(videoAd -> new VideoAdResponseDto(
                        videoAd.getVideo().getId(),
                        videoAd.getAdvertisement().getId(),
                        videoAd.getAdStartTime()
                ))
                .collect(Collectors.toList());
    }

    // 특정 비디오에서 광고 제거
    public void removeVideoAd(Long videoId, Long adId) {
        VideoAdId videoAdId = new VideoAdId(videoId, adId);
        if (!videoAdRepository.existsById(videoAdId)) {
            throw new RuntimeException("Relation not found");
        }
        videoAdRepository.deleteById(videoAdId);
    }
}
