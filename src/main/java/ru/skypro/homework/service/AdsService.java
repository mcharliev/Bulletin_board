package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAdsDto;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.dto.ResponseWrapperAdsDto;

public interface AdsService {
    FullAdsDto getFullAdsById(Integer id);

    AdsDto createAds(CreateAdsDto createAds);

    void delete(Integer id);

    ResponseWrapperAdsDto getAllAds();

    CreateAdsDto editAds(Integer id, AdsDto adsDto);

    ResponseWrapperAdsDto getAllMyAds(Authentication authentication);
}
