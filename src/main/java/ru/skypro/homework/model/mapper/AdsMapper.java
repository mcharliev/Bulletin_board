package ru.skypro.homework.model.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAdsDto;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.model.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdsMapper {

    public FullAdsDto adsEntityAndUserEntityToFullAdsDto(AdsEntity adsEntity, UserEntity author) {
        FullAdsDto fullAdsDto = new FullAdsDto();
        fullAdsDto.setPk(adsEntity.getId());
        fullAdsDto.setPrice(adsEntity.getPrice());
        fullAdsDto.setTitle(adsEntity.getTitle());
        fullAdsDto.setDescription(adsEntity.getDescription());
        fullAdsDto.setAuthorFirstName(author.getFirstName());
        fullAdsDto.setAuthorLastName(author.getLastName());
        fullAdsDto.setEmail(author.getEmail());
        fullAdsDto.setPhone(author.getPhone());
        ImageEntity imageEntity = adsEntity.getImage();
        if (imageEntity != null) {
            fullAdsDto.setImage(String.format("/ads/%s/image", adsEntity.getImage().getId()));
        }
        return fullAdsDto;
    }

    public AdsDto adsEntityToAdsDto(AdsEntity entity) {
        AdsDto dto = new AdsDto();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setPk(entity.getId());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        ImageEntity imageEntity = entity.getImage();
        if (imageEntity != null) {
            dto.setImage(String.format("/ads/%s/image", entity.getImage().getId()));
        }
        return dto;
    }

    public AdsEntity createAdsToAdsEntity(CreateAdsDto dto) {
        AdsEntity adsEntity = new AdsEntity();
        adsEntity.setPrice(dto.getPrice());
        adsEntity.setDescription(dto.getDescription());
        adsEntity.setTitle(dto.getTitle());
        return adsEntity;
    }

    public ResponseWrapperAdsDto adsListToResponseWrapperAdsDto(List<AdsEntity> entityList) {
        List<AdsDto> dtoList = entityList.stream().map(this::adsEntityToAdsDto).collect(Collectors.toList());
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        responseWrapperAdsDto.setCount(dtoList.size());
        responseWrapperAdsDto.setResults(dtoList);
        return responseWrapperAdsDto;
    }
}
