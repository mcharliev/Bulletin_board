package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto update(UserDto userDto) {
        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        userRepository.save(userEntity);
        return userDto;
    }
}
