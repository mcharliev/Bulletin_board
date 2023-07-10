package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.AccessDeniedException;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.dto.CommentDto;
import ru.skypro.homework.model.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.entity.CommentEntity;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс - сервис, содержащий реализацию интерфейса {@link CommentService}
 * @see CommentEntity
 * @see CommentRepository
 */
@RequiredArgsConstructor
@Service
public class CommentServiceImp implements CommentService {
    private final AdsService adsService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final UserService userService;


    /**
     * Метод создает комментарий к объявлению по id объявления
     * @return {@link CommentRepository#save(Object)},
     * {@link CommentMapper#commentEntityToCommentDto(CommentEntity)}
     * @throws AdsNotFoundException  если объявление по указанному id не найдено
     * @throws UserNotFoundException если пользователь не найден
     * @see CommentMapper
     */
    @Override
    public CommentDto createComment(Integer id, CommentDto commentDto, Authentication authentication) {
        AdsEntity adsEntity = adsService.getAdsEntity(id);
        UserEntity userEntity = userService.getUserEntity(authentication);
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(commentDto.getText());
        commentEntity.setAds(adsEntity);
        commentEntity.setAuthor(userEntity);
        commentEntity.setCreateAt(LocalDateTime.now());
        commentRepository.save(commentEntity);
        return commentMapper.commentEntityToCommentDto(commentEntity);
    }

    /**
     * Метод ищет и возвращает список всех комментариев
     * {@link ResponseWrapperCommentDto} к объявлению по id объявления
     * {@link CommentRepository#findAllByAdsId(Integer)}
     * {@link CommentMapper#commentEntityListToWrapperCommentDto(List)}  }
     */
    @Override
    public ResponseWrapperCommentDto getAllAdsComment(Integer id) {
        AdsEntity adsEntity = adsService.getAdsEntity(id);
        List<CommentEntity> commentEntityList = commentRepository.findAllByAdsId(adsEntity.getId());
        return commentMapper.commentEntityListToWrapperCommentDto(commentEntityList);
    }

    /**
     * Метод удаляет комментарий к объявлению по id объявления
     * {@link CommentRepository#delete(Object)}
     * @throws AccessDeniedException если нет прав на удаление комментария
     */
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

    /**
     * Метод редактирует комментарий к объявлению по id
     * @return {@link CommentRepository#save(Object)}, {@link CommentMapper#commentEntityToCommentDto(CommentEntity)}
     * @throws CommentNotFoundException если комментарий не найден
     * @throws AccessDeniedException    если нет прав на обновление комментария
     * @see CommentMapper
     */
    @Override
    public CommentDto editComment(Integer adId, Integer commentId, CommentDto comment, Authentication authentication) {
        if (checkRights(commentId, authentication)) {
            String text = comment.getText();
            CommentEntity commentEntity = commentRepository.findByAdsIdAndId(adId, commentId)
                    .orElseThrow(CommentNotFoundException::new);
            commentEntity.setText(text);
            commentEntity.setCreateAt(LocalDateTime.now());
            commentRepository.save(commentEntity);
            return commentMapper.commentEntityToCommentDto(commentEntity);
        } else {
            throw new AccessDeniedException();
        }
    }

    /**
     * Метод проверяет наличие доступа к редактированию или удалению комментария по id
     * @throws CommentNotFoundException если комментарий не найден
     */
    private boolean checkRights(Integer id, Authentication authentication) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        UserEntity currentUser = userService.getUserEntity(authentication);
        String authorCommentLogin = commentEntity.getAuthor().getEmail();
        Role currentUserRole = currentUser.getRole();
        String currentUserLogin = authentication.getName();
        return authorCommentLogin.equals(currentUserLogin) || currentUserRole == Role.ADMIN;
    }
}
