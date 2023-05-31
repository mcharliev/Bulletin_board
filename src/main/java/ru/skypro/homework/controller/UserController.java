package ru.skypro.homework.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;


@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(new User());
    }

    @GetMapping("/me")
    public ResponseEntity<User> updateUser() {
        return ResponseEntity.ok(new User());
    }

    @PostMapping("/set_password")
    public ResponseEntity<NewPassword> updatePassword(@RequestBody NewPassword newPassword) {
        return ResponseEntity.ok(new NewPassword());
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile image) {
        return ResponseEntity.ok("File uploaded successfully");
    }
}


