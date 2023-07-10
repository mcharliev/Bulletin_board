package ru.skypro.homework.model.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.model.dto.RegisterReqDto;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.model.entity.UserEntity;

@Component
public class UserMapper {

    public UserDto userEntityToUserDto(UserEntity userEntity) {
        UserDto dto = new UserDto();
        dto.setId(userEntity.getId());
        dto.setEmail(userEntity.getEmail());
        dto.setFirstName(userEntity.getFirstName());
        dto.setLastName(userEntity.getLastName());
        dto.setPhone(userEntity.getPhone());
        ImageEntity imageEntity = userEntity.getImage();
        if (imageEntity != null) {
            dto.setImage(String.format("/users/%s/image", userEntity.getImage().getId()));
        }
        return dto;
    }

    public UserEntity registerReqDtoToUserEntity(RegisterReqDto reqDto) {
        UserEntity entity = new UserEntity();
        entity.setFirstName(reqDto.getFirstName());
        entity.setEmail(reqDto.getUsername());
        entity.setRole(Role.USER);
        entity.setLastName(reqDto.getLastName());
        entity.setPhone(reqDto.getPhone());
        entity.setPassword(reqDto.getPassword());
        return entity;
    }
}
