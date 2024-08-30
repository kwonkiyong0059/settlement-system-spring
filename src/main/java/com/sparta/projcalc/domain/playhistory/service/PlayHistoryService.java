package com.sparta.projcalc.domain.playhistory.service;

import com.sparta.projcalc.domain.playhistory.dto.request.AddPlayHistoryRequestDto;
import com.sparta.projcalc.domain.playhistory.dto.response.PlayHistoryResponseDto;
import com.sparta.projcalc.domain.playhistory.entity.PlayHistory;
import com.sparta.projcalc.domain.playhistory.exception.PlayHistoryNotFoundException;
import com.sparta.projcalc.domain.playhistory.repository.PlayHistoryRepository;
import com.sparta.projcalc.domain.user.entity.User;
import com.sparta.projcalc.domain.user.exception.UserNotFoundException;
import com.sparta.projcalc.domain.user.repository.UserRepository;
import com.sparta.projcalc.domain.video.entity.Video;
import com.sparta.projcalc.domain.video.exception.VideoNotFoundException;
import com.sparta.projcalc.domain.video.repository.VideoRepository;
import com.sparta.projcalc.domain.adview.service.AdViewHistoryService;
import com.sparta.projcalc.domain.adview.dto.request.CreateAdViewHistoryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlayHistoryService {

    private final PlayHistoryRepository playHistoryRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final AdViewHistoryService adViewHistoryService;

    // 재생 기록 추가
    public PlayHistoryResponseDto addPlayHistory(AddPlayHistoryRequestDto requestDto) {
        // 사용자와 비디오 조회
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        Video video = videoRepository.findById(requestDto.getVideoId())
                .orElseThrow(() -> new VideoNotFoundException("비디오를 찾을 수 없습니다."));

        // 비디오 조회수 증가
        video.incrementViews();
        videoRepository.save(video);

        // 재생 기록 생성
        LocalDateTime now = LocalDateTime.now();
        PlayHistory playHistory = new PlayHistory(user, video, requestDto.getPlaybackPosition(), now, now);
        PlayHistory created = playHistoryRepository.save(playHistory);

        // 광고 조회 기록 생성 (광고가 비디오 중간에 표시될 경우)
        if (requestDto.getAdId() != null) { // 광고 ID가 제공된 경우만 처리
            CreateAdViewHistoryRequestDto adViewRequestDto = new CreateAdViewHistoryRequestDto(
                    user.getId(),
                    requestDto.getAdId(),
                    now
            );
            adViewHistoryService.createAdViewHistory(adViewRequestDto);
        }

        return PlayHistoryResponseDto.builder()
                .id(created.getId())
                .userId(created.getUser().getId())
                .videoId(created.getVideo().getId())
                .playbackPosition(created.getPlaybackPosition())
                .watchedAt(created.getWatchedAt())
                .build();
    }

    // 재생 기록 조회
    public PlayHistoryResponseDto getPlayHistory(Long id) {
        PlayHistory playHistory = playHistoryRepository.findById(id)
                .orElseThrow(() -> new PlayHistoryNotFoundException("재생 기록을 찾을 수 없습니다."));

        return PlayHistoryResponseDto.builder()
                .id(playHistory.getId())
                .userId(playHistory.getUser().getId())
                .videoId(playHistory.getVideo().getId())
                .playbackPosition(playHistory.getPlaybackPosition())
                .watchedAt(playHistory.getWatchedAt())
                .build();
    }

    // 재생 기록 업데이트
    public PlayHistoryResponseDto updatePlayHistory(Long id, Long playbackPosition) {
        PlayHistory playHistory = playHistoryRepository.findById(id)
                .orElseThrow(() -> new PlayHistoryNotFoundException("재생 기록을 찾을 수 없습니다."));

        playHistory.updatePlaybackPosition(playbackPosition); // 재생 위치 및 시청 시간 업데이트
        PlayHistory updated = playHistoryRepository.save(playHistory);

        return PlayHistoryResponseDto.builder()
                .id(updated.getId())
                .userId(updated.getUser().getId())
                .videoId(updated.getVideo().getId())
                .playbackPosition(updated.getPlaybackPosition())
                .watchedAt(updated.getWatchedAt())
                .build();
    }

    // 재생 기록 삭제
    public void deletePlayHistory(Long id) {
        if (!playHistoryRepository.existsById(id)) {
            throw new PlayHistoryNotFoundException("재생 기록을 찾을 수 없습니다.");
        }
        playHistoryRepository.deleteById(id);
    }
}
