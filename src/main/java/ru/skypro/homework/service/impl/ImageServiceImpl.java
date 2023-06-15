package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    @Override
    public ImageEntity updateUserImage(MultipartFile image) {
        try {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setData(image.getBytes());
            imageEntity.setFileSize(image.getSize());
            imageEntity.setMediaType(image.getContentType());
            return imageRepository.save(imageEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
