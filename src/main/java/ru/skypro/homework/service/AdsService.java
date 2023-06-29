package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAdsDto;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.model.entity.AdsEntity;

public interface AdsService {
    FullAdsDto getFullAdsById(Integer id);

    AdsDto createAds(CreateAdsDto createAds,MultipartFile file, Authentication authentication);

    void delete(Integer id,Authentication authentication);

    ResponseWrapperAdsDto getAllAds(String title);

    CreateAdsDto editAds(Integer id, AdsDto adsDto,Authentication authentication);

    ResponseWrapperAdsDto getAllMyAds(Authentication authentication);

    AdsEntity getAdsEntity(Integer adsId);

    String updateAdsImage(Integer id, MultipartFile image);
}
