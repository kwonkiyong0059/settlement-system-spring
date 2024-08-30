package com.sparta.projcalc.batch.statistics;

import com.sparta.projcalc.domain.video.entity.Video;
import com.sparta.projcalc.domain.video.repository.VideoRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * 배치 처리할 비디오 엔티티를 데이터베이스에서 읽어오는 ItemReader입니다.
 */
@Component
public class VideoStatisticsItemReader implements ItemReader<Video> {

    private final VideoRepository videoRepository;
    private Iterator<Video> videoIterator;

    /**
     * VideoStatisticsItemReader의 생성자입니다.
     *
     * @param videoRepository 비디오 데이터를 접근하는 데 사용되는 저장소.
     */
    public VideoStatisticsItemReader(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    /**
     * 다음 비디오 엔티티를 읽어옵니다.
     *
     * @return 다음 비디오 엔티티 또는 더 이상 아이템이 없으면 null.
     */
    @Override
    public Video read() {
        if (videoIterator == null) {
            videoIterator = videoRepository.findAll().iterator(); // Iterator 초기화.
        }
        return videoIterator.hasNext() ? videoIterator.next() : null; // 다음 아이템 반환 또는 null.
    }
}
