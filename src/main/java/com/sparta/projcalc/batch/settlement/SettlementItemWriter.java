package com.sparta.projcalc.batch.settlement;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class SettlementItemWriter implements ItemWriter<Settlement> {

    private final SettlementRepository settlementRepository;

    public SettlementItemWriter(SettlementRepository settlementRepository) {
        this.settlementRepository = settlementRepository;
    }

    @Override
    public void write(Chunk<? extends Settlement> items) throws Exception {
        settlementRepository.saveAll(items.getItems());
    }
}

