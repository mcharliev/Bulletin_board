package ru.skypro.homework.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.entity.AdsEntity;


@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "author", ignore = true)
    AdsEntity adsDtoToAdsEntity(AdsDto dto);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk", source = "id")
    AdsDto adsEntityToAdsDto(AdsEntity entity);
}
