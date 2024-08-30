package com.sparta.projcalc.domain.adview.controller;

import com.sparta.projcalc.domain.adview.dto.request.CreateAdViewHistoryRequestDto;
import com.sparta.projcalc.domain.adview.dto.response.AdViewHistoryResponseDto;
import com.sparta.projcalc.domain.adview.service.AdViewHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ad_view_history")
@RequiredArgsConstructor
public class AdViewHistoryController {

    private final AdViewHistoryService adViewHistoryService;

    @PostMapping
    public ResponseEntity<AdViewHistoryResponseDto> addAdViewHistory(
            @Valid @RequestBody CreateAdViewHistoryRequestDto requestDto) {
        AdViewHistoryResponseDto responseDto = adViewHistoryService.createAdViewHistory(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<List<AdViewHistoryResponseDto>> getAdViewHistories(@PathVariable Long adId) {
        List<AdViewHistoryResponseDto> responseDtos = adViewHistoryService.getAdViewHistories(adId);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
