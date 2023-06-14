package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPasswordDto;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
                                              Authentication authentication) {
        return ResponseEntity.ok(userService.update(userDto,authentication));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> updateUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserDto(authentication));
    }

    @PostMapping("/set_password")
    public ResponseEntity<NewPasswordDto> updatePassword(@RequestBody NewPasswordDto newPassword,
                                                         Authentication authentication) {
        return ResponseEntity.ok(userService.setPassword(newPassword,authentication));
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile image,
                                              Authentication authentication) {
        try {
            return ResponseEntity.ok(userService.uploadUserImage(image,authentication));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


