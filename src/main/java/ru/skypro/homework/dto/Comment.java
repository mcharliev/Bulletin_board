package ru.skypro.homework.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createAt;
    private Integer pk;
    private String text;
}
