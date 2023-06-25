package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.RegisterReqDto;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public boolean login(String userName, String password) {
        UserDetails userDetails = userService.loadUserByUsername(userName);
        return passwordEncoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(RegisterReqDto registerReq, Role role) {
        if (userRepository.findByEmail(registerReq.getUsername()).isPresent()) {
            return false;
        }
        registerReq.setRole(role);
        UserEntity userEntity = userMapper.registerReqDtoToUserEntity(registerReq);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return true;
    }
}
