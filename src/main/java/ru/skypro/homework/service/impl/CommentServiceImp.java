package ru.skypro.homework.service.impl;

import liquibase.pro.packaged.R;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.entity.CommentEntity;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImp implements CommentService {
    private final AdsService adsService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    public CommentDto createComment(Integer id, CommentDto commentDto, Authentication authentication) {
        CommentEntity commentEntity = commentMapper.commentDtoToCommentEntity(commentDto);
        AdsEntity adsEntity = adsService.getAdsEntity(id);
        UserEntity userEntity = userService.getUserEntity(authentication);
        commentEntity.setAds(adsEntity);
        commentEntity.setAuthor(userEntity);
        commentEntity.setLocalDateTime(LocalDateTime.now());
        commentRepository.save(commentEntity);
        return commentMapper.commentEntityToCommentDto(commentEntity);
    }

    @Override
    public ResponseWrapperCommentDto getAllAdsComment(Integer id) {
        AdsEntity adsEntity = adsService.getAdsEntity(id);
        List<CommentEntity> adsCommentsList = commentRepository.findAllByAdsId(adsEntity.getId());
        ResponseWrapperCommentDto responseWrapperCommentDto = new ResponseWrapperCommentDto();
        responseWrapperCommentDto.setCount(adsCommentsList.size());
        responseWrapperCommentDto.setResults(commentMapper.toDtoList(adsCommentsList));
        return responseWrapperCommentDto;
    }

    @Transactional
    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteByAdsIdAndId(adId, commentId);
    }

    @Override
    public CommentDto editComment(Integer adId, Integer commentId, CommentDto comment) {
        String text = comment.getText();
        CommentEntity commentEntity = commentRepository.findByAdsIdAndId(adId, commentId);
        commentEntity.setText(text);
        commentEntity.setLocalDateTime(LocalDateTime.now());
        commentRepository.save(commentEntity);
        return commentMapper.commentEntityToCommentDto(commentEntity);
    }
}
