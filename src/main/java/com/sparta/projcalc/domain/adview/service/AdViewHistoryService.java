package com.sparta.projcalc.domain.adview.service;

import com.sparta.projcalc.domain.adview.dto.request.CreateAdViewHistoryRequestDto;
import com.sparta.projcalc.domain.adview.dto.response.AdViewHistoryResponseDto;
import com.sparta.projcalc.domain.adview.entity.AdViewHistory;
import com.sparta.projcalc.domain.adview.repository.AdViewHistoryRepository;
import com.sparta.projcalc.domain.ad.entity.Advertisement;
import com.sparta.projcalc.domain.ad.exception.AdvertisementNotFoundException;
import com.sparta.projcalc.domain.ad.repository.AdvertisementRepository;
import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.domain.user.exception.UserNotFoundException;
import com.sparta.projcalc.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdViewHistoryService {

    private final AdViewHistoryRepository adViewHistoryRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    // 광고 시청 기록 추가
    @Transactional
    public AdViewHistoryResponseDto createAdViewHistory(CreateAdViewHistoryRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        Advertisement advertisement = advertisementRepository.findById(requestDto.getAdId())
                .orElseThrow(() -> new AdvertisementNotFoundException("광고를 찾을 수 없습니다."));

        AdViewHistory adViewHistory = new AdViewHistory(user, advertisement, requestDto.getViewedAt());
        adViewHistoryRepository.save(adViewHistory);

        return AdViewHistoryResponseDto.builder()
                .id(adViewHistory.getId())
                .userId(adViewHistory.getUser().getId())
                .adId(adViewHistory.getAdvertisement().getId())
                .viewedAt(adViewHistory.getViewedAt())
                .build();
    }

    // 광고 시청 기록 조회
    public List<AdViewHistoryResponseDto> getAdViewHistories(Long adId) {
        List<AdViewHistory> adViewHistories = adViewHistoryRepository.findByAdvertisementId(adId);

        return adViewHistories.stream()
                .map(adViewHistory -> AdViewHistoryResponseDto.builder()
                        .id(adViewHistory.getId())
                        .userId(adViewHistory.getUser().getId())
                        .adId(adViewHistory.getAdvertisement().getId())
                        .viewedAt(adViewHistory.getViewedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
