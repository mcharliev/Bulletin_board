package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.UserDto;

@Service
public interface UserService {
    UserDto update(UserDto userDto);
}
