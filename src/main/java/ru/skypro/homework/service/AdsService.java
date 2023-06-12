package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAdsDto;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.model.entity.AdsEntity;

public interface AdsService {
    FullAdsDto getFullAdsById(Integer id);

    AdsDto createAds(CreateAdsDto createAds,Authentication authentication);

    void delete(Integer id);

    ResponseWrapperAdsDto getAllAds();

    CreateAdsDto editAds(Integer id, AdsDto adsDto);

    ResponseWrapperAdsDto getAllMyAds(Authentication authentication);

    AdsEntity getAdsEntity(Integer adsId);
}
