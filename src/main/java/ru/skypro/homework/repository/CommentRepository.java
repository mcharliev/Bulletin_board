package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.entity.CommentEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findAllByAdsId(Integer id);

    void deleteByAdsIdAndId(Integer adId,Integer commentId);

    CommentEntity findByAdsIdAndId(Integer adId, Integer commentId);
}
