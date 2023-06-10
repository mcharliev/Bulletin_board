package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.NewPasswordDto;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto update(UserDto userDto, Authentication authentication) {
        UserEntity oldUserEntity = userRepository.findByEmail(authentication.getName()).get();
        UserEntity newUserEntity = userMapper.userDtoToUserEntity(userDto);
        newUserEntity.setPassword(oldUserEntity.getPassword());
        userRepository.save(newUserEntity);
        return userDto;
    }

    @Override
    public UserDto getUser(Authentication authentication) {
        String email = authentication.getName();
        UserEntity userEntity = userRepository.findByEmail(email).get();
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public NewPasswordDto setPassword(NewPasswordDto newPasswordDto, Authentication authentication) {
//        UserEntity userEntity = userRepository.findByEmail(authentication.getName()).get();
//        userEntity.setPassword(newPasswordDto.getNewPassword());
//        userRepository.save(userEntity);
        return null;
    }


}
