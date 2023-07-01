package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.AccessDeniedException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.entity.CommentEntity;
import ru.skypro.homework.model.entity.ImageEntity;
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
        CommentDto dto = commentMapper.commentEntityToCommentDto(commentEntity);
        ImageEntity imageEntity = commentEntity.getAuthor().getImage();
        if (imageEntity != null) {
            dto.setAuthorImage(String.format("/ads/%s/image", userEntity.getImage().getId()));
        }
        return dto;
    }

    @Override
    public ResponseWrapperCommentDto getAllAdsComment(Integer id) {
        AdsEntity adsEntity = adsService.getAdsEntity(id);

        List<CommentEntity> adsCommentsList = commentRepository.findAllByAdsId(adsEntity.getId());

        ResponseWrapperCommentDto responseWrapperCommentDto = createCommentDtoList(adsCommentsList);
        return responseWrapperCommentDto;
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) {
        if (checkRights(commentId, authentication)) {
            CommentEntity commentEntity = commentRepository.findByAdsIdAndId(adId, commentId)
                    .orElseThrow(CommentNotFoundException::new);
            commentRepository.delete(commentEntity);
        } else {
            throw new AccessDeniedException();
        }
    }

    @Override
    public CommentDto editComment(Integer adId, Integer commentId, CommentDto comment, Authentication authentication) {
        if (checkRights(commentId, authentication)) {
            String text = comment.getText();
            CommentEntity commentEntity = commentRepository.findByAdsIdAndId(adId, commentId)
                    .orElseThrow(CommentNotFoundException::new);
            commentEntity.setText(text);
            commentEntity.setLocalDateTime(LocalDateTime.now());
            commentRepository.save(commentEntity);
            return commentMapper.commentEntityToCommentDto(commentEntity);
        } else {
            throw new AccessDeniedException();
        }
    }

    private boolean checkRights(Integer id, Authentication authentication) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        UserEntity currentUser = userService.getUserEntity(authentication);
        String authorCommentLogin = commentEntity.getAuthor().getEmail();
        Role currentUserRole = currentUser.getRole();
        String currentUserLogin = authentication.getName();
        return authorCommentLogin.equals(currentUserLogin) || currentUserRole == Role.ADMIN;
    }

    private ResponseWrapperCommentDto createCommentDtoList(List<CommentEntity> commentEntityList) {

        List<CommentDto> commentDtoList = commentMapper.toDtoList(commentEntityList);

        for (int i = 0; i < commentDtoList.size(); i++) {
            ImageEntity imageEntity = commentEntityList.get(i).getAuthor().getImage();
            if (imageEntity != null) {
                String authorImageLink = String.format("/users/%s/image",
                        commentEntityList.get(i).getAuthor().getImage().getId());
                commentDtoList.get(i).setAuthorImage(authorImageLink);
            }
        }
        ResponseWrapperCommentDto responseWrapperCommentDto = new ResponseWrapperCommentDto();
        responseWrapperCommentDto.setCount(commentDtoList.size());
        responseWrapperCommentDto.setResults(commentDtoList);

        return responseWrapperCommentDto;
    }
}
