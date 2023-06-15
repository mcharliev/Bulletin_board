package ru.skypro.homework.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.dto.RegisterReqDto;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserEntity;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userEntityToUserDto(UserEntity entity);

    UserEntity userDtoToUserEntity(UserDto dto);

    @Mapping(target = "email",source = "username")
    UserEntity registerReqDtoToUserEntity(RegisterReqDto registerReqDto);
}
