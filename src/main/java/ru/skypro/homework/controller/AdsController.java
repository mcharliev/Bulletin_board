package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.*;
import ru.skypro.homework.model.entity.ImageEntity;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.ImageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {
    private final AdsService adsService;
    private final CommentService commentService;
    private final ImageService imageService;

    @Operation(
            summary = "Получить все объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperAdsDto.class))
                    })
            },
            tags = "Ads")
    @GetMapping
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds(@RequestParam(required = false) String title) {
        return ResponseEntity.ok(adsService.getAllAds(title));
    }

    @Operation(
            summary = "Добавить объявление",

            responses = {
                    @ApiResponse(responseCode = "201", description = "Created", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            },
            tags = "Ads")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> createAds(
            @RequestPart("image") MultipartFile file,
            @RequestPart("properties") CreateAdsDto createAds,
            Authentication authentication) {
        return ResponseEntity.ok(adsService.createAds(createAds, file, authentication));
    }

    @Operation(
            summary = "Получить информацию об объявлении",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = FullAdsDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Ads not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            },
            tags = "Ads")
    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getFullAdsById(id));
    }

    @Operation(
            summary = "Удалить объявление",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Ads not found")
            },
            tags = "Ads")
    @DeleteMapping("/{id}")
    public void deleteAds(@PathVariable Integer id,
                          Authentication authentication) {
        adsService.delete(id, authentication);
    }

    @Operation(
            summary = "Обновить информацию об объявлении",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CreateAdsDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Ads not found")
            },
            tags = "Ads")
    @PatchMapping("/{id}")
    public ResponseEntity<CreateAdsDto> updateAds(@PathVariable Integer id,
                                                  @RequestBody AdsDto ads,
                                                  Authentication authentication) {
        return ResponseEntity.ok(adsService.editAds(id, ads, authentication));
    }

    @Operation(
            summary = "Получить объявления авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperAdsDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Ads not found")
            },
            tags = "Ads")
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adsService.getAllMyAds(authentication));
    }

    @Operation(summary = "Обновить картинку объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Картинка объявления обновлена",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schema = @Schema(type = "string"))
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Ads not found")
            },
            tags = "Ads"
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAdsImage(@PathVariable Integer id,
                                                 @RequestParam MultipartFile image) {
        return ResponseEntity.ok(adsService.updateAdsImage(id, image));
    }

    @Operation(
            summary = "Получить комментарии объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ResponseWrapperCommentDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Comment not Found")
            },
            tags = "Comment"
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseWrapperCommentDto> getAdsComments(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getAllAdsComment(id));
    }

    @Operation(
            summary = "Добавить комментарий к объявлению",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Ads not found")
            },
            tags = "Comment"
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> createAdsComment(@PathVariable Integer id,
                                                       @RequestBody CommentDto comment,
                                                       Authentication authentication) {
        return ResponseEntity.ok(commentService.createComment(id, comment, authentication));
    }

    @Operation(
            summary = "Удалить комментарий",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            },
            tags = "Comment"
    )
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteAdsComment(@PathVariable("adId") Integer adId,
                                 @PathVariable("commentId") Integer commentId,
                                 Authentication authentication) {
        commentService.deleteComment(adId, commentId, authentication);
    }

    @Operation(
            summary = "Обновить комментарий",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDto.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            },
            tags = "Comment"
    )
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> editAdsComment(@PathVariable("adId") Integer adId,
                                                     @PathVariable("commentId") Integer commentId,
                                                     @RequestBody CommentDto comment,
                                                     Authentication authentication) {
        return ResponseEntity.ok(commentService.editComment(adId, commentId, comment, authentication));
    }

    @Operation(
            summary = "Получить картинку объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = ImageEntity.class))),

                    @ApiResponse(responseCode = "404", description = "Image not found")
            },
            tags = "Ads"
    )
    @GetMapping(value = "/{id}/image", produces = {MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE, "image/*"})
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }
}
