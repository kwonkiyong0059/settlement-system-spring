package com.sparta.projcalc.domain.ad.service;

import com.sparta.projcalc.domain.ad.dto.request.CreateAdvertisementRequestDto;
import com.sparta.projcalc.domain.ad.dto.request.UpdateAdvertisementRequestDto;
import com.sparta.projcalc.domain.ad.dto.response.AdvertisementResponseDto;
import com.sparta.projcalc.domain.ad.entity.Advertisement;
import com.sparta.projcalc.domain.ad.exception.AdvertisementNotFoundException;
import com.sparta.projcalc.domain.ad.repository.AdvertisementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    @Transactional
    public AdvertisementResponseDto createAdvertisement(CreateAdvertisementRequestDto createDto) {
        Advertisement advertisement = Advertisement.builder()
                .title(createDto.getTitle())
                .content(createDto.getContent())
                .imageUrl(createDto.getImageUrl())
                .viewCount(0L)
                .build();

        advertisementRepository.save(advertisement);

        return AdvertisementResponseDto.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .content(advertisement.getContent())
                .imageUrl(advertisement.getImageUrl())
                .viewCount(advertisement.getViewCount())
                .createdAt(advertisement.getCreatedAt())
                .updatedAt(advertisement.getUpdatedAt())
                .build();
    }

    @Transactional
    public AdvertisementResponseDto updateAdvertisement(Long adId, UpdateAdvertisementRequestDto updateDto) {
        Advertisement advertisement = advertisementRepository.findById(adId)
                .orElseThrow(() -> new AdvertisementNotFoundException("광고를 찾을 수 없습니다."));

        // updateDetails 메서드를 사용하여 광고 정보를 업데이트
        advertisement.updateDetails(updateDto.getTitle(), updateDto.getContent(), updateDto.getImageUrl());

        advertisementRepository.save(advertisement);

        return AdvertisementResponseDto.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .content(advertisement.getContent())
                .imageUrl(advertisement.getImageUrl())
                .viewCount(advertisement.getViewCount())
                .createdAt(advertisement.getCreatedAt())
                .updatedAt(advertisement.getUpdatedAt())
                .build();
    }

    public AdvertisementResponseDto getAdvertisement(Long adId) {
        Advertisement advertisement = advertisementRepository.findById(adId)
                .orElseThrow(() -> new AdvertisementNotFoundException("광고를 찾을 수 없습니다."));

        // 조회수 증가
        advertisement.incrementViewCount();
        advertisementRepository.save(advertisement);

        return AdvertisementResponseDto.builder()
                .id(advertisement.getId())
                .title(advertisement.getTitle())
                .content(advertisement.getContent())
                .imageUrl(advertisement.getImageUrl())
                .viewCount(advertisement.getViewCount())
                .createdAt(advertisement.getCreatedAt())
                .updatedAt(advertisement.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteAdvertisement(Long adId) {

        Advertisement advertisement = advertisementRepository.findById(adId)
                .orElseThrow(() -> new AdvertisementNotFoundException("광고를 찾을 수 없습니다."));

        advertisementRepository.delete(advertisement);
    }
}
