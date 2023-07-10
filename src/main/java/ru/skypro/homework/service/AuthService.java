package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.RegisterReqDto;
import ru.skypro.homework.model.dto.Role;

public interface AuthService {
    boolean login(String userName, String password);
    boolean register(RegisterReqDto registerReq, Role role);
}
