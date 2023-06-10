package ru.skypro.homework.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.NewPasswordDto;
import ru.skypro.homework.model.dto.UserDto;
import ru.skypro.homework.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> updateUser() {
        return ResponseEntity.ok(new UserDto());
    }

    @PostMapping("/set_password")
    public ResponseEntity<NewPasswordDto> updatePassword(@RequestBody NewPasswordDto newPassword) {
        return ResponseEntity.ok(new NewPasswordDto());
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile image) {
        return ResponseEntity.ok("File uploaded successfully");
    }
}


