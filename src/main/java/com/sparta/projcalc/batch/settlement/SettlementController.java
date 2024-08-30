package com.sparta.projcalc.batch.settlement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementRepository settlementRepository;

    public SettlementController(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    @GetMapping("/daily")
    public ResponseEntity<List<Settlement>> getDailySettlement(@RequestParam LocalDate date) {
        List<Settlement> settlements = settlementRepository.findAllBySettlementDate(date);
        return ResponseEntity.ok(settlements);
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<Settlement>> getWeeklySettlement(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Settlement> settlements = settlementRepository.findAllBySettlementDateBetween(startDate, endDate);
        return ResponseEntity.ok(settlements);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<Settlement>> getMonthlySettlement(@RequestParam LocalDate month) {
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        List<Settlement> settlements = settlementRepository.findAllBySettlementDateBetween(startDate, endDate);
        return ResponseEntity.ok(settlements);
    }
}
