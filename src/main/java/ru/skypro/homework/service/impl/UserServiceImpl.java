package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.dto.NewPasswordDto;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;

    @Override
    public UserDto update(UserDto userDto, Authentication authentication) {
        UserEntity oldUserEntity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        UserEntity newUserEntity = userMapper.userDtoToUserEntity(userDto);
        newUserEntity.setPassword(oldUserEntity.getPassword());
        userRepository.save(newUserEntity);
        return userDto;
    }

    @Override
    public UserDto getUserDto(Authentication authentication) {
        String email = authentication.getName();
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.userEntityToUserDto(userEntity);
    }

    @Override
    public NewPasswordDto setPassword(NewPasswordDto newPasswordDto, Authentication authentication) {
//        UserEntity userEntity = userRepository.findByEmail(authentication.getName()).get();
//        userEntity.setPassword(newPasswordDto.getNewPassword());
//        userRepository.save(userEntity);
        return null;
    }

    @Override
    public UserEntity getUserEntity(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public String uploadUserImage(MultipartFile image, Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        user.setAvatar(imageService.updateUserImage(image));
        return "OK";
    }
}
