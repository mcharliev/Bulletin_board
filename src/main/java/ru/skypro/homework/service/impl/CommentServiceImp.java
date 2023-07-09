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
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Класс - сервис, содержащий реализацию интерфейса {@link CommentService}
 *
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
     *
     * @return {@link CommentRepository#save(Object)}, {@link CommentMapper#commentEntityToCommentDto(CommentEntity)}
     * @throws AdsNotFoundException  если объявление по указанному id не найдено
     * @throws UserNotFoundException если пользователь не найден
     * @see CommentMapper
     */
    @Override
    public CommentDto createComment(Integer id, CommentDto commentDto, Authentication authentication) {
        CommentEntity commentEntity = new CommentEntity();
        AdsEntity adsEntity = adsService.getAdsEntity(id);
        UserEntity userEntity = userService.getUserEntity(authentication);
        commentEntity.setText(commentDto.getText());
        commentEntity.setAds(adsEntity);
        commentEntity.setAuthor(userEntity);
        commentEntity.setCreateAt(LocalDateTime.now());
        commentRepository.save(commentEntity);
        CommentDto dto = commentMapper.commentEntityToCommentDto(commentEntity);
        dto.setAuthorFirstName(commentEntity.getAuthor().getFirstName());
        dto.setCreateAt(commentEntity.getCreateAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        ImageEntity imageEntity = commentEntity.getAuthor().getImage();
        if (imageEntity != null) {
            dto.setAuthorImage(String.format("/users/%s/image", userEntity.getImage().getId()));
        }
        return dto;
    }

    /**
     * Метод ищет и возвращает список всех комментариев {@link ResponseWrapperCommentDto} к объявлению по id объявления
     * <p>
     * {@link CommentRepository#findAllByAdsId(Integer)}
     * <p>
     * {@link #createCommentDtoList(List<CommentEntity>) method}  }
     */
    @Override
    public ResponseWrapperCommentDto getAllAdsComment(Integer id) {
        AdsEntity adsEntity = adsService.getAdsEntity(id);
        List<CommentEntity> adsCommentsList = commentRepository.findAllByAdsId(adsEntity.getId());
        return createCommentDtoList(adsCommentsList);
    }

    /**
     * Метод удаляет комментарий к объявлению по id объявления
     * {@link CommentRepository#delete(Object)}
     *
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
     *
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
     *
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

    /**
     * Метод переносит данные из List<CommentEntity> в List<CommentDto>
     * <p>
     * * @return {ResponseWrapperCommentDto}
     */
    private ResponseWrapperCommentDto createCommentDtoList(List<CommentEntity> commentEntityList) {

        List<CommentDto> commentDtoList = commentMapper.toDtoList(commentEntityList);

        for (int i = 0; i < commentDtoList.size(); i++) {
            commentDtoList.get(i).setCreateAt(commentEntityList.get(i).getCreateAt()
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            commentDtoList.get(i).setAuthorFirstName(commentEntityList.get(i).getAuthor().getFirstName());
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
