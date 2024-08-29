package com.sparta.projcalc.domain.video.repository;

import com.sparta.projcalc.domain.video.entity.Video;
import com.sparta.projcalc.domain.video.entity.VideoCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    // 카테고리로 비디오 조회
    List<Video> findByCategory(VideoCategory category);

    // 제목으로 비디오 조회 (대소문자 구분 없이 제목에 부분 문자열이 포함된 비디오를 검색)
    List<Video> findByTitleContainingIgnoreCase(String title);
}
