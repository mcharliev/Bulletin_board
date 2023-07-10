package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.AccessDeniedException;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.model.dto.*;
import ru.skypro.homework.model.entity.AdsEntity;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.model.entity.UserEntity;
import ru.skypro.homework.model.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Класс - сервис, содержащий реализацию интерфейса {@link AdsService}
 *
 * @see AdsEntity
 * @see AdsRepository
 */
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {
    private final AdsRepository adsRepository;
    private final AdsMapper adsMapper;
    private final UserService userService;
    private final ImageService imageService;
    private final CommentRepository commentRepository;


    /**
     * Метод ищет и возвращает полную информацию объявления по id
     * {@link AdsRepository#findById(Object)}
     * {@link AdsMapper#adsEntityAndUserEntityToFullAdsDto(AdsEntity, UserEntity)}
     * @return FullAdsDto
     * @throws AdsNotFoundException если объявление не найдено
     */
    public FullAdsDto getFullAdsById(Integer id) {
        AdsEntity adsEntity = adsRepository.findById(id)
                .orElseThrow(AdsNotFoundException::new);
        UserEntity author = adsEntity.getAuthor();
        return adsMapper.adsEntityAndUserEntityToFullAdsDto(adsEntity, author);
    }

    /**
     * Метод создает объявление
     * {@link UserService#getUserEntity(Authentication)}
     * {@link ImageService#saveImage(MultipartFile)}
     * @return AdsDto
     */
    @Override
    public AdsDto createAds(CreateAdsDto createAds,
                            MultipartFile image,
                            Authentication authentication) {
        AdsEntity adsEntity = adsMapper.createAdsToAdsEntity(createAds);
        UserEntity user = userService.getUserEntity(authentication);
        ImageEntity imageEntity = imageService.saveImage(image);
        adsEntity.setAuthor(user);
        adsEntity.setImage(imageEntity);
        adsRepository.save(adsEntity);
        return adsMapper.adsEntityToAdsDto(adsEntity);
    }

    /**
     * Метод удаляет объявление по id
     * {@link AdsRepository#delete(Object)}
     */
    @Transactional
    @Override
    public void delete(Integer id, Authentication authentication) {
        if (checkRights(id, authentication)) {
            AdsEntity adsEntity = adsRepository.findById(id)
                    .orElseThrow(AdsNotFoundException::new);
            imageService.deleteImage(adsEntity.getImage());
            commentRepository.deleteAllByAdsId(id);
            adsRepository.deleteById(id);
        } else {
            throw new AccessDeniedException();
        }
    }

    /**
     * Метод ищет и возвращает список всех объявлений
     * {@link AdsRepository#findAll()}
     * {@link AdsRepository#findByTitleContainingIgnoreCaseOrderByTitle(String)}
     * @return ResponseWrapperAdsDto
     */
    @Override
    public ResponseWrapperAdsDto getAllAds(String title) {
        List<AdsEntity> adsEntityList;
        if (title == null) {
            adsEntityList = adsRepository.findAll();
        } else {
            adsEntityList = adsRepository.findByTitleContainingIgnoreCaseOrderByTitle(title);
        }
        return adsMapper.adsListToResponseWrapperAdsDto(adsEntityList);
    }

    /**
     * Метод редактирует объявление по id
     * {@link AdsRepository#findById(Object)}
     * {@link AdsRepository#save(Object)}
     * @return {@link AdsMapper#adsEntityToAdsDto(AdsEntity)}
     * @throws AdsNotFoundException  если объявление не найдено
     * @throws AccessDeniedException если нет прав на обновление комментария
     */
    @Override
    public AdsDto editAds(Integer id, CreateAdsDto createAdsDto, Authentication authentication) {
        if (checkRights(id, authentication)) {
            AdsEntity adsEntity = adsRepository.findById(id)
                    .orElseThrow(AdsNotFoundException::new);
            adsEntity.setTitle(createAdsDto.getTitle());
            adsEntity.setDescription(createAdsDto.getDescription());
            adsEntity.setPrice(createAdsDto.getPrice());
            adsRepository.save(adsEntity);
            return adsMapper.adsEntityToAdsDto(adsEntity);
        } else {
            throw new AccessDeniedException();
        }
    }

    /**
     * Метод ищет и возвращает список всех объявлений авторизированного пользователя
     * {@link UserService#getUserEntity(Authentication)}
     * {@link AdsRepository#findAdsEntitiesByAuthorId(Integer)}
     * {@link AdsMapper#adsListToResponseWrapperAdsDto(List)}  }
     * @return ResponseWrapperAdsDto
     */
    @Override
    public ResponseWrapperAdsDto getAllMyAds(Authentication authentication) {
        Integer authorId = userService.getUserEntity(authentication).getId();
        List<AdsEntity> adsEntityList = adsRepository.findAdsEntitiesByAuthorId(authorId);
        return adsMapper.adsListToResponseWrapperAdsDto(adsEntityList);
    }

    /**
     * Метод достает сущность объявления из база данных по id
     * @return {@link AdsRepository#findById(Object)}
     * @throws AdsNotFoundException если объяввление не найдено
     */
    @Override
    public AdsEntity getAdsEntity(Integer adsId) {
        return adsRepository.findById(adsId).orElseThrow(AdsNotFoundException::new);
    }

    /**
     * Метод достает объявление из базы данных,
     * устанавливает или обновляет его картинку, затем сохраняет изменения в базе данных:
     * {@link ImageRepository#saveAndFlush(Object)}
     * {@link AdsRepository#save(Object)}
     * @return String
     * @throws AdsNotFoundException если объявление не найдено
     */
    @Override
    public String updateAdsImage(Integer id, MultipartFile image) {
        AdsEntity adsEntity = getAdsEntity(id);
        ImageEntity oldImage = adsEntity.getImage();
        if (oldImage == null) {
            ImageEntity newImage = imageService.saveImage(image);
            adsEntity.setImage(newImage);
            adsRepository.saveAndFlush(adsEntity);
            return "image uploaded successfully";
        } else {
            ImageEntity updatedImage = imageService.updateImage(image, oldImage);
            adsEntity.setImage(updatedImage);
            adsRepository.saveAndFlush(adsEntity);
            return "image uploaded successfully";
        }
    }

    /**
     * Метод проверяет наличие доступа к редактированию или удалению объявления по id
     * @throws AdsNotFoundException если объявление не найдено
     */
    private boolean checkRights(Integer id, Authentication authentication) {
        AdsEntity adsEntity = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        UserEntity currentUser = userService.getUserEntity(authentication);
        String authorAdsLogin = adsEntity.getAuthor().getEmail();
        Role currentUserRole = currentUser.getRole();
        String currentUserLogin = authentication.getName();
        return authorAdsLogin.equals(currentUserLogin) || currentUserRole == Role.ADMIN;
    }
}
