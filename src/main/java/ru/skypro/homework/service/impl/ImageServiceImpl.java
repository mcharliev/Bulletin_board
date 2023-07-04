package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    /**
     * Метод сохраняет картинку в базу данных
     *
     * @return {@link ImageRepository#saveAndFlush(Object)}
     */
    @Override
    public ImageEntity saveImage(MultipartFile image) {
        ImageEntity newImage = new ImageEntity();
        try {
            byte[] bytes = image.getBytes();
            newImage.setData(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newImage.setId(UUID.randomUUID().toString());
        return imageRepository.saveAndFlush(newImage);
    }

    /**
     * Метод обновляет и сохраняет картинку в базу данных
     *
     * @return {@link ImageRepository#saveAndFlush(Object)}
     */
    @Override
    public ImageEntity updateImage(MultipartFile newImage, ImageEntity oldImage) {
        try {
            byte[] bytes = newImage.getBytes();
            oldImage.setData(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageRepository.saveAndFlush(oldImage);
    }

    /**
     * Метод достает картинку из базы данных по ее id {
     *
     * @return {@link ImageRepository#findById(String)}
     * @throws ImageNotFoundException если картинка не найдена
     */
    @Override
    public byte[] getImageById(String id) {
        ImageEntity imageEntity = imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
        return imageEntity.getData();
    }

    /**
     * Метод достает Optional из базы данных по ее id {
     *
     * @return {@link ImageRepository#findById(String)}
     */
    @Override
    public Optional<ImageEntity> findImageEntityById(String id) {
        return imageRepository.findById(id);
    }
}
