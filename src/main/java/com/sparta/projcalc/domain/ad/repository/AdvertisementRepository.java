package com.sparta.projcalc.domain.ad.repository;

import com.sparta.projcalc.domain.ad.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByTitleContainingIgnoreCase(String title);

    List<Advertisement> findByContentContainingIgnoreCase(String content);

}

