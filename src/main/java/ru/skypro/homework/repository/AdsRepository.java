package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.entity.AdsEntity;

import java.util.List;

public interface AdsRepository extends JpaRepository<AdsEntity, Integer> {
    List<AdsEntity> findAdsEntitiesByAuthorId(Integer authorId);
}
