package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.entity.ImageEntity;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    Optional<ImageEntity> findById(String id);
}
