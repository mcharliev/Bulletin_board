package ru.skypro.homework.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserEntity;


@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "id", source = "id")
    UserDto userEntityToUserDto(UserEntity entity);


//    @Mapping(target = "id", source = "id")
    UserEntity userDtoToUserEntity(UserDto dto);
}
