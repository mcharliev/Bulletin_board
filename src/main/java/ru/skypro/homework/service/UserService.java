package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.NewPasswordDto;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserEntity;

@Service
public interface UserService {
    UserDto update(UserDto userDto,Authentication authentication);

    UserDto getUserDto(Authentication authentication);

    NewPasswordDto setPassword(NewPasswordDto newPassword,Authentication authentication);

    UserEntity getUserEntity(Authentication authentication);
}
