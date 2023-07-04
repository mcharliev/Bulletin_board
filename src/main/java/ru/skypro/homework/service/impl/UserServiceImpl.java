package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ChangePasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.dto.NewPasswordDto;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.UserEntityDetails;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

/**
 * Класс - сервис, содержащий реализацию интерфейса {@link UserService} и {@link UserDetailsService}
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final PasswordEncoder encoder;


    /**
     * Метод достает пользователя из базы данных {@link UserRepository#findByEmail(String)},
     * редактирует данные и сохраняет в базе
     *
     * @return {@link UserRepository#save(Object)}, {@link UserMapper#userEntityToUserDto(UserEntity)}
     * @see UserMapper
     */
    @Override
    public UserDto update(UserDto userDto, Authentication authentication) {
        UserEntity oldUserEntity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        UserEntity newUserEntity = userMapper.userDtoToUserEntity(userDto);
        newUserEntity.setPassword(oldUserEntity.getPassword());
        newUserEntity.setRole(oldUserEntity.getRole());
        userRepository.save(newUserEntity);
        return userDto;
    }

    /**
     * Метод достает пользователя из базы данных {@link UserRepository#findByEmail(String)}
     * и конвертирует его в {@link UserDto}
     *
     * @return {@link UserMapper#userEntityToUserDto(UserEntity)}
     */
    @Override
    public UserDto getUserDto(Authentication authentication) {
        String email = authentication.getName();
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        UserDto userDto = userMapper.userEntityToUserDto(userEntity);
        ImageEntity imageEntity = userEntity.getImage();
        if (imageEntity != null) {
            userDto.setImage(String.format("/users/%s/image", userEntity.getImage().getId()));
        }
        return userDto;
    }

    /**
     * Метод меняет пароль {@link PasswordEncoder#encode(CharSequence)}
     *
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public NewPasswordDto setPassword(NewPasswordDto newPasswordDto, Authentication authentication) {
        UserEntity userEntity = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        if (encoder.matches(userEntity.getPassword(), newPasswordDto.getCurrentPassword())) {
            userEntity.setPassword(encoder.encode(newPasswordDto.getNewPassword()));
            userRepository.save(userEntity);
            NewPasswordDto dto = new NewPasswordDto();
            dto.setCurrentPassword(userEntity.getPassword());
            dto.setNewPassword(newPasswordDto.getNewPassword());
            return dto;
        } else {
            throw new ChangePasswordException();
        }
    }

    /**
     * Метод достает сущность UserEntity из базе данных
     *
     * @return {@link UserRepository#findByEmail(String)}
     * @throws UserNotFoundException если пользователь не найден
     */
    @Override
    public UserEntity getUserEntity(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Метод находит пользователя по email и возвращает его данные: имя пользователя и пароль
     *
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optUserEntity = userRepository.findByEmail(username);
        if (optUserEntity.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new UserEntityDetails(optUserEntity.get());
    }

    /**
     * Метод достает пользователя из базы данных,
     * устанавливает или обновляет его аватар, затем сохраняет изменения в базе данных:
     * {@link ImageRepository#saveAndFlush(Object)}, {@link UserRepository#save(Object)}
     *
     * @throws UserNotFoundException если пользователь не найден
     */
    public String updateUserImage(MultipartFile image, Authentication authentication) {
        UserEntity userEntity = getUserEntity(authentication);
        ImageEntity oldImage = userEntity.getImage();
        if (oldImage == null) {
            ImageEntity newImage = imageService.saveImage(image);
            userEntity.setImage(newImage);
            userRepository.save(userEntity);
            return "image uploaded successfully";
        } else {
            ImageEntity updatedImage = imageService.updateImage(image, oldImage);
            userEntity.setImage(updatedImage);
            userRepository.save(userEntity);
            return "image uploaded successfully";
        }
    }
}
