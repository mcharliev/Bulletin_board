package ru.skypro.homework.model.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface FullAdsMapper {
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName",source = "author.firstName")
    @Mapping(target = "authorLastName",source = "author.lastName")
    @Mapping(target = "phone",source = "author.phone")
    @Mapping(target = "email",source = "author.email")
    FullAdsDto adsEntityToFullAdsDto(AdsEntity adsEntity);

    FullAdsDto toFullAdsDto(UserEntity userEntity);

    @AfterMapping
    default void afterToDto(@MappingTarget FullAdsDto dto, AdsEntity adsEntity) {
        dto = toFullAdsDto(adsEntity.getAuthor());
    }
}
