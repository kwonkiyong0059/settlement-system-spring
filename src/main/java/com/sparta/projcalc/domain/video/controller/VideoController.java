package com.sparta.projcalc.domain.video.controller;

import com.sparta.projcalc.domain.video.dto.request.CreateVideoRequestDto;
import com.sparta.projcalc.domain.video.dto.request.UpdateVideoRequestDto;
import com.sparta.projcalc.domain.video.dto.response.SimpleVideoResponseDto;
import com.sparta.projcalc.domain.video.dto.response.VideoResponseDto;
import com.sparta.projcalc.domain.video.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping
    public ResponseEntity<VideoResponseDto> createVideo(@Valid @RequestBody CreateVideoRequestDto createDto) {
        VideoResponseDto response = videoService.createVideo(createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{videoId}")
    public ResponseEntity<VideoResponseDto> updateVideo(
            @PathVariable Long videoId,
            @Valid @RequestBody UpdateVideoRequestDto updateDto) {
        VideoResponseDto response = videoService.updateVideo(videoId, updateDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoResponseDto> getVideo(@PathVariable Long videoId) {
        VideoResponseDto response = videoService.getVideo(videoId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SimpleVideoResponseDto>> getAllVideos() {
        List<SimpleVideoResponseDto> response = videoService.getAllVideos();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
