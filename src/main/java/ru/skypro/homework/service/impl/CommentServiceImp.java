package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.entity.CommentEntity;
import ru.skypro.homework.model.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

@RequiredArgsConstructor
@Service
public class CommentServiceImp implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto createComment(Integer id, CommentDto commentDto) {
        CommentEntity commentEntity = commentMapper.commentDtoToCommentEntity(commentDto);
        commentEntity.setId(id);
        commentRepository.save(commentEntity);
        return commentDto;
    }
}
