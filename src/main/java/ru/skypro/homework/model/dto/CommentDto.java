package ru.skypro.homework.model.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Integer author;
    private String authorImage;
    private String authorFirstName;
    private Long createAt;
    private Integer pk;
    private String text;
}
