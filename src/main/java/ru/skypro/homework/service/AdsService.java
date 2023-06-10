package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.FullAdsDto;

public interface AdsService {
    FullAdsDto getFullAdsById(Integer id);

    AdsDto createAds(Integer id, AdsDto adsDto);
}
