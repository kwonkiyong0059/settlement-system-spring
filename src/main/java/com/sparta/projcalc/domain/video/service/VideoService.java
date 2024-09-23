package com.sparta.projcalc.domain.video.service;

import com.sparta.projcalc.domain.video.dto.request.CreateVideoRequestDto;
import com.sparta.projcalc.domain.video.dto.request.UpdateVideoRequestDto;
import com.sparta.projcalc.domain.video.dto.response.SimpleVideoResponseDto;
import com.sparta.projcalc.domain.video.dto.response.VideoResponseDto;
import com.sparta.projcalc.domain.video.entity.Video;
import com.sparta.projcalc.domain.video.entity.VideoCategory;
import com.sparta.projcalc.domain.video.repository.VideoRepository;
import com.sparta.projcalc.domain.video.exception.VideoNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    @Transactional
    public VideoResponseDto createVideo(CreateVideoRequestDto createDto) {
        Video video = Video.builder()
                .title(createDto.getTitle())
                .description(createDto.getDescription())
                .url(createDto.getUrl())
                .category(createDto.getCategory())
                .duration(createDto.getDuration())
                .build();

        videoRepository.save(video);

        return VideoResponseDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .url(video.getUrl())
                .category(VideoCategory.valueOf(video.getCategory().name()))
                .duration(video.getDuration())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .build();
    }

    @Transactional
    public VideoResponseDto updateVideo(Long videoId, UpdateVideoRequestDto updateDto) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("비디오를 찾을 수 없습니다."));

        video.updateVideo(updateDto.getTitle(), updateDto.getDescription(), updateDto.getUrl(), updateDto.getCategory(), updateDto.getDuration());

        return VideoResponseDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .url(video.getUrl())
                .category(VideoCategory.valueOf(video.getCategory().name()))
                .duration(video.getDuration())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .build();
    }

    public VideoResponseDto getVideo(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("비디오를 찾을 수 없습니다."));

        // 조회할 때 views 값을 증가시킴
        video.incrementViews();

        // 증가된 views 값을 저장
        videoRepository.save(video);

        return VideoResponseDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .url(video.getUrl())
                .category(VideoCategory.valueOf(video.getCategory().name()))
                .duration(video.getDuration())
                .createdAt(video.getCreatedAt())
                .updatedAt(video.getUpdatedAt())
                .build();
    }

    @Transactional
    public List<SimpleVideoResponseDto> getAllVideos() {
        List<Video> videos = videoRepository.findAll();

        return videos.stream()
                .map(video -> SimpleVideoResponseDto.builder()
                        .id(video.getId())
                        .title(video.getTitle())
                        .url(video.getUrl())
                        .duration(video.getDuration())
                        .build())
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteVideo(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("비디오를 찾을 수 없습니다."));
        videoRepository.delete(video);
    }

}
