package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.FullAdsDto;

public interface AdsService {
    FullAdsDto getAdsById(Integer id);
}
