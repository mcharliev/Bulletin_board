package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.mapper.FullAdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {
    private final AdsRepository adsRepository;
    private final FullAdsMapper fullAdsMapper;

    @Override
    public FullAdsDto getAdsById(Integer id) {
        AdsEntity adsEntity = adsRepository.findById(id).get();
        FullAdsDto fullAdsDto = fullAdsMapper.AdsEntityToFullAdsDto(adsEntity);
        return fullAdsDto;
    }
}
