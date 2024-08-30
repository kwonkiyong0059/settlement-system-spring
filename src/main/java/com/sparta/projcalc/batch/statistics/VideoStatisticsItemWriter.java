package com.sparta.projcalc.batch.statistics;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * 비디오 통계 객체를 데이터베이스에 저장하는 ItemWriter입니다.
 */
@Component
public class VideoStatisticsItemWriter implements ItemWriter<VideoStatistics> {

    private static final Logger logger = Logger.getLogger(VideoStatisticsItemWriter.class.getName());
    private final VideoStatisticsRepository statisticsRepository;

    /**
     * VideoStatisticsItemWriter의 생성자입니다.
     *
     * @param statisticsRepository 통계를 저장할 저장소.
     */
    public VideoStatisticsItemWriter(VideoStatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * 비디오 통계 리스트를 데이터베이스에 저장합니다.
     *
     * @param chunk 저장할 VideoStatistics 객체 Chunk.
     */
    @Override
    public void write(Chunk<? extends VideoStatistics> chunk) {
        List<? extends VideoStatistics> items = chunk.getItems();
        try {
            statisticsRepository.saveAll(items); // 통계 객체 리스트를 저장합니다.
            logger.info("통계 객체 " + items.size() + "개가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            logger.severe("통계 객체 저장 중 오류 발생: " + e.getMessage());
            throw e; // 예외를 다시 던져서 배치 처리에 영향을 미치도록 합니다.
        }
    }
}
