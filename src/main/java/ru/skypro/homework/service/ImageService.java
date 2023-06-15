package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.entity.ImageEntity;

public interface ImageService {
    ImageEntity updateUserImage(MultipartFile avatar);
}
