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
     *
     * @return FullAdsDto
     * @throws AdsNotFoundException если объявление не найдено
     */
    public FullAdsDto getFullAdsById(Integer id) {
        AdsEntity adsEntity = adsRepository.findById(id)
                .orElseThrow(AdsNotFoundException::new);
        FullAdsDto fullAdsDto = adsMapper.adsEntityToFullAdsDto(adsEntity);
        fullAdsDto.setImage(String.format("/ads/%s/image", adsEntity.getImage().getId()));
        return fullAdsDto;
    }

    /**
     * Метод создает объявление
     * {@link UserService#getUserEntity(Authentication)}
     * {@link AdsMapper#createAdsDtoToAdsEntity(CreateAdsDto)}
     * {@link ImageService#saveImage(MultipartFile)}
     *
     * @return AdsDto
     */
    @Override
    public AdsDto createAds(CreateAdsDto createAds,
                            MultipartFile image,
                            Authentication authentication) {
        UserEntity user = userService.getUserEntity(authentication);
        AdsEntity adsEntity = adsMapper.createAdsDtoToAdsEntity(createAds);
        ImageEntity imageEntity = imageService.saveImage(image);
        adsEntity.setAuthor(user);
        adsEntity.setImage(imageEntity);
        adsRepository.save(adsEntity);
        AdsDto adsDto = adsMapper.adsEntityToAdsDto(adsEntity);
        adsDto.setImage(String.format("/ads/%s/image", adsEntity.getImage().getId()));
        return adsDto;
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
     *
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
        List<AdsDto> adsDtoList = insertLinkToAdsDtoList(adsEntityList);
        return createResponseWrapperAdsDto(adsDtoList.size(), adsDtoList);
    }

    /**
     * Метод редактирует объявление по id
     * {@link AdsRepository#findById(Object)}
     * {@link AdsMapper#editAdsEntity(AdsDto, AdsEntity)}
     * {@link AdsRepository#save(Object)}
     *
     * @return {@link AdsMapper#adsEntityToCreateAdsDto(AdsEntity)}
     * @throws AdsNotFoundException  если объявление не найдено
     * @throws AccessDeniedException если нет прав на обновление комментария
     */
    @Override
    public CreateAdsDto editAds(Integer id, AdsDto adsDto, Authentication authentication) {
        if (checkRights(id, authentication)) {
            AdsEntity adsEntity = adsRepository.findById(id)
                    .orElseThrow(AdsNotFoundException::new);
            adsMapper.editAdsEntity(adsDto, adsEntity);
            adsRepository.save(adsEntity);
            return adsMapper.adsEntityToCreateAdsDto(adsEntity);
        } else {
            throw new AccessDeniedException();
        }
    }

    /**
     * Метод ищет и возвращает список всех объявлений авторизированного пользователя
     * {@link UserService#getUserEntity(Authentication)}
     * {@link AdsRepository#findAdsEntitiesByAuthorId(Integer)}
     * {@link #insertLinkToAdsDtoList(List)} method}  }
     *
     * @return ResponseWrapperAdsDto
     */
    @Override
    public ResponseWrapperAdsDto getAllMyAds(Authentication authentication) {
        Integer authorId = userService.getUserEntity(authentication).getId();
        List<AdsEntity> adsEntityList = adsRepository.findAdsEntitiesByAuthorId(authorId);
        List<AdsDto> adsDtoList = insertLinkToAdsDtoList(adsEntityList);
        return createResponseWrapperAdsDto(adsDtoList.size(), adsDtoList);
    }

    /**
     * Метод достает сущность объявления из база данных по id
     *
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
     * {@link ImageRepository#saveAndFlush(Object)}, {@link AdsRepository#save(Object)}
     *
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
     * Метод переносит данные из List<AdsDto> в ResponseWrapperAdsDto
     *
     * @return ResponseWrapperAdsDto
     */
    private ResponseWrapperAdsDto createResponseWrapperAdsDto(Integer count, List<AdsDto> adsDtoList) {
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        responseWrapperAdsDto.setCount(count);
        responseWrapperAdsDto.setResults(adsDtoList);
        return responseWrapperAdsDto;
    }

    /**
     * Метод проверяет наличие доступа к редактированию или удалению объявления по id
     *
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

    /**
     * Метод переносит данные из List<AdsEntity> в List<AdsDto>
     *
     * @return AdsDto
     */
    private List<AdsDto> insertLinkToAdsDtoList(List<AdsEntity> adsEntityList) {
        List<AdsDto> adsDtoList = adsMapper.toDtoList(adsEntityList);
        for (int i = 0; i < adsDtoList.size(); i++) {
            String imageList = String.format("/ads/%s/image", adsEntityList.get(i).getImage().getId());
            adsDtoList.get(i).setImage(imageList);
        }
        return adsDtoList;
    }
}
