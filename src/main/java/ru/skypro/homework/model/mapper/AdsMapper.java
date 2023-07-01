package ru.skypro.homework.model.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.model.dto.AdsDto;
import ru.skypro.homework.model.dto.CreateAdsDto;
import ru.skypro.homework.model.dto.FullAdsDto;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.entity.UserEntity;

import java.util.Collection;
import java.util.List;


@Mapper(componentModel = "spring")
public interface AdsMapper {

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    AdsEntity adsDtoToAdsEntity(AdsDto dto);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "image", ignore = true)
    AdsDto adsEntityToAdsDto(AdsEntity entity);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "image", ignore = true)
    FullAdsDto adsEntityToFullAdsDto(AdsEntity adsEntity);
    @Mapping(target = "image", ignore = true)
    FullAdsDto toFullAdsDto(UserEntity userEntity);

    @AfterMapping
    default void afterToDto(@MappingTarget FullAdsDto dto, AdsEntity adsEntity) {
        dto = toFullAdsDto(adsEntity.getAuthor());
    }

    AdsEntity createAdsDtoToAdsEntity(CreateAdsDto createAdsDto);

    CreateAdsDto adsEntityToCreateAdsDto(AdsEntity adsEntity);

    List<AdsEntity> toEntityList(Collection<AdsDto> adsDtoList);

    List<AdsDto> toDtoList(Collection<AdsEntity> adsEntitiesList);


    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    void editAdsEntity(AdsDto adsDto, @MappingTarget AdsEntity ads);
}
