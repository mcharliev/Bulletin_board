package ru.skypro.homework.model.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.entity.AdsEntity;


@Mapper(componentModel = "spring")
public interface AdsMapper {
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk", source = "id")
    AdsDto adsEntityToAdsDto(AdsEntity entity);

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "author.id", source = "author")
    AdsEntity AdsDtoToAdsEntity(AdsDto dto);

    @AfterMapping
    default void afterAdsDtoToAdsEntity(@MappingTarget AdsDto adsDto, AdsEntity adsEntity){
        adsDto.setAuthor(adsEntity.getAuthor().getId());
    }
}
