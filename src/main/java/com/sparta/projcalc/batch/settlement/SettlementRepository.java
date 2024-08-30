package com.sparta.projcalc.batch.settlement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long> {
    List<Settlement> findAllBySettlementDate(LocalDate settlementDate);
    List<Settlement> findAllBySettlementDateBetween(LocalDate startDate, LocalDate endDate);
}