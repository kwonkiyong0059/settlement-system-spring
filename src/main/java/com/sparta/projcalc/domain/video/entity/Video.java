package com.sparta.projcalc.domain.video.entity;

import com.sparta.projcalc.domain.ad.entity.Advertisement;
import com.sparta.projcalc.domain.playhistory.entity.PlayHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    private VideoCategory category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long views;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlayHistory> playHistories; // 비디오와 관련된 시청 기록

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Advertisement> advertisements; // 광고 목록

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementViews() {
        this.views++;
    }

    public void updateVideo(String title, String description, String url, VideoCategory category) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.category = category;
    }
}
