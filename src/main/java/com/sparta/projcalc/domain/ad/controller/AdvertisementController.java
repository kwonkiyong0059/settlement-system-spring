package com.sparta.projcalc.domain.ad.controller;

import com.sparta.projcalc.domain.ad.dto.request.CreateAdvertisementRequestDto;
import com.sparta.projcalc.domain.ad.dto.request.UpdateAdvertisementRequestDto;
import com.sparta.projcalc.domain.ad.dto.response.AdvertisementResponseDto;
import com.sparta.projcalc.domain.ad.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    @PostMapping
    public ResponseEntity<AdvertisementResponseDto> createAdvertisement(@RequestBody CreateAdvertisementRequestDto createDto) {
        AdvertisementResponseDto responseDto = advertisementService.createAdvertisement(createDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementResponseDto> updateAdvertisement(@PathVariable Long id, @RequestBody UpdateAdvertisementRequestDto updateDto) {
        AdvertisementResponseDto responseDto = advertisementService.updateAdvertisement(id, updateDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponseDto> getAdvertisement(@PathVariable Long id) {
        AdvertisementResponseDto responseDto = advertisementService.getAdvertisement(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id) {
        advertisementService.deleteAdvertisement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
