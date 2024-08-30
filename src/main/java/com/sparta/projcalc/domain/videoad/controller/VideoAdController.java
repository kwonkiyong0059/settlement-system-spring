package com.sparta.projcalc.domain.videoad.controller;

import com.sparta.projcalc.domain.videoad.dto.request.CreateVideoAdRequestDto;
import com.sparta.projcalc.domain.videoad.dto.response.VideoAdResponseDto;
import com.sparta.projcalc.domain.videoad.service.VideoAdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/video_ads")
@RequiredArgsConstructor
public class VideoAdController {

    private final VideoAdService videoAdService;

    @PostMapping
    public ResponseEntity<VideoAdResponseDto> addVideoAd(@RequestBody CreateVideoAdRequestDto requestDto) {
        VideoAdResponseDto responseDto = videoAdService.addVideoAd(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VideoAdResponseDto>> getAllVideoAds() {
        List<VideoAdResponseDto> responseDtos = videoAdService.getAllVideoAds();
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{videoId}/{adId}")
    public ResponseEntity<Void> removeVideoAd(@PathVariable Long videoId, @PathVariable Long adId) {
        videoAdService.removeVideoAd(videoId, adId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
