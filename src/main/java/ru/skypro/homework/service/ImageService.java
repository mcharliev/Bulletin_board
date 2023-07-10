package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.entity.ImageEntity;

import java.util.Optional;

public interface ImageService {
    ImageEntity saveImage(MultipartFile image);

    ImageEntity updateImage(MultipartFile newImage, ImageEntity oldImage);

    byte[] getImageById(String id);
    Optional<ImageEntity> findImageEntityById(String id);

    void deleteImage(ImageEntity imageEntity);
}
