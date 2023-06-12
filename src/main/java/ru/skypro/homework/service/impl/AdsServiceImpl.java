package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAdsDto;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {
    private final AdsRepository adsRepository;

    private final AdsMapper adsMapper;
    private final UserService userService;

    public FullAdsDto getFullAdsById(Integer id) {
        AdsEntity adsEntity = adsRepository.findById(id).get();
        FullAdsDto fullAdsDto = adsMapper.adsEntityToFullAdsDto(adsEntity);
        return fullAdsDto;
    }

    @Override
    public AdsDto createAds(CreateAdsDto createAds,
                            Authentication authentication) {
        UserEntity user = userService.getUserEntity(authentication);
        AdsEntity adsEntity = adsMapper.createAdsDtoToAdsEntity(createAds);
        adsEntity.setAuthor(user);
        adsRepository.save(adsEntity);
        return adsMapper.adsEntityToAdsDto(adsEntity);
    }

    @Override
    public void delete(Integer id) {
        AdsEntity adsEntity = adsRepository.findById(id).get();
        adsRepository.delete(adsEntity);
    }

    @Override
    public ResponseWrapperAdsDto getAllAds() {
        List<AdsEntity> adsEntityList = adsRepository.findAll();
        List<AdsDto> adsDtoList = adsMapper.toDtoList(adsEntityList);
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        responseWrapperAdsDto.setCount(adsDtoList.size());
        responseWrapperAdsDto.setResults(adsDtoList);
        return responseWrapperAdsDto;
    }

    @Override
    public CreateAdsDto editAds(Integer id, AdsDto adsDto) {
        AdsEntity adsEntity = adsRepository.findById(id).get();
        adsMapper.editAdsEntity(adsDto, adsEntity);
        adsRepository.save(adsEntity);
        return adsMapper.adsEntityToCreateAdsDto(adsEntity);
    }

    @Override
    public ResponseWrapperAdsDto getAllMyAds(Authentication authentication) {
        Integer authorId = userService.getUserEntity(authentication).getId();
        List<AdsEntity> adsEntityList = adsRepository.findAdsEntitiesByAuthorId(authorId);
        List<AdsDto> adsDtoList = adsMapper.toDtoList(adsEntityList);
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        responseWrapperAdsDto.setCount(adsDtoList.size());
        responseWrapperAdsDto.setResults(adsDtoList);
        return responseWrapperAdsDto;
    }

    @Override
    public AdsEntity getAdsEntity(Integer adsId) {
        return adsRepository.findById(adsId).get();
    }
}
