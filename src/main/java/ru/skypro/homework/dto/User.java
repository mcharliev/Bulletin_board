package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String firthName;
    private String lastName;
    private String phone;
    private String image;
}