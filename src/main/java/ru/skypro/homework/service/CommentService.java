package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.ResponseWrapperCommentDto;

public interface CommentService {
    CommentDto createComment(Integer id, CommentDto commentDto, Authentication authentication);

    ResponseWrapperCommentDto getAllAdsComment(Integer id);

    void deleteComment(Integer adId, Integer commentId,Authentication authentication);

    CommentDto editComment(Integer adId, Integer commentId, CommentDto comment,Authentication authentication);
}
