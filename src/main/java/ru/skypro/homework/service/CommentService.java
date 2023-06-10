package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(Integer id, CommentDto commentDto);
}
