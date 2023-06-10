package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.mapper.AdsMapper;
import ru.skypro.homework.model.mapper.FullAdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {
    private final AdsRepository adsRepository;
    private final FullAdsMapper fullAdsMapper;
    private final AdsMapper adsMapper;


    public FullAdsDto getFullAdsById(Integer id) {
        AdsEntity adsEntity = adsRepository.findById(id).get();
        FullAdsDto fullAdsDto = fullAdsMapper.adsEntityToFullAdsDto(adsEntity);
        return fullAdsDto;
    }

    @Override
    public AdsDto createAds(Integer id, AdsDto adsDto) {
        AdsEntity adsEntity = adsMapper.adsDtoToAdsEntity(adsDto);
        adsRepository.save(adsEntity);
        return adsDto;
    }
}
