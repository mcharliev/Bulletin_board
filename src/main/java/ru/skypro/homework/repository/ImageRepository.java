package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity,Integer> {

}
