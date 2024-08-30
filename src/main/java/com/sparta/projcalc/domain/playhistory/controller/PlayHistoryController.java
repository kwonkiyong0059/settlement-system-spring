package com.sparta.projcalc.domain.playhistory.controller;

import com.sparta.projcalc.domain.playhistory.dto.response.PlayHistoryResponseDto;
import com.sparta.projcalc.domain.playhistory.dto.request.AddPlayHistoryRequestDto;
import com.sparta.projcalc.domain.playhistory.dto.request.UpdatePlayHistoryRequestDto;
import com.sparta.projcalc.domain.playhistory.service.PlayHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playback_history")
@RequiredArgsConstructor
public class PlayHistoryController {

    private final PlayHistoryService playHistoryService;

    @PostMapping
    public ResponseEntity<PlayHistoryResponseDto> addPlayHistory(
            @Valid @RequestBody AddPlayHistoryRequestDto requestDto) {
        PlayHistoryResponseDto responseDto = playHistoryService.addPlayHistory(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayHistoryResponseDto> getPlayHistory(@PathVariable Long id) {
        PlayHistoryResponseDto responseDto = playHistoryService.getPlayHistory(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayHistoryResponseDto> updatePlayHistory(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePlayHistoryRequestDto requestDto) {
        PlayHistoryResponseDto responseDto = playHistoryService.updatePlayHistory(id, requestDto.getPlaybackPosition());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayHistory(@PathVariable Long id) {
        playHistoryService.deletePlayHistory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
