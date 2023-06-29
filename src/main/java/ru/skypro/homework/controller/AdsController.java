package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.dto.*;
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

    @GetMapping
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds(@RequestParam(required = false)String title) {
        return ResponseEntity.ok(adsService.getAllAds(title));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> createAds(
            @RequestPart("image") MultipartFile file,
            @RequestPart("properties") CreateAdsDto createAds,
            Authentication authentication) {
        return ResponseEntity.ok(adsService.createAds(createAds,file, authentication));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAdsDto> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getFullAdsById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteAds(@PathVariable Integer id,
                          Authentication authentication) {
        adsService.delete(id,authentication);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CreateAdsDto> updateAds(@PathVariable Integer id,
                                                  @RequestBody AdsDto ads,
                                                  Authentication authentication) {
        return ResponseEntity.ok(adsService.editAds(id, ads,authentication));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adsService.getAllMyAds(authentication));
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAdsImage(@PathVariable Integer id,
                                                 @RequestParam MultipartFile image) {
        return ResponseEntity.ok(adsService.updateAdsImage(id,image));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseWrapperCommentDto> getAdsComments(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getAllAdsComment(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> createAdsComment(@PathVariable Integer id,
                                                       @RequestBody CommentDto comment,
                                                       Authentication authentication) {
        return ResponseEntity.ok(commentService.createComment(id, comment, authentication));
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteAdsComment(@PathVariable("adId") Integer adId,
                                 @PathVariable("commentId") Integer commentId,
                                 Authentication authentication) {
        commentService.deleteComment(adId, commentId,authentication);
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDto> editAdsComment(@PathVariable("adId") Integer adId,
                                                     @PathVariable("commentId") Integer commentId,
                                                     @RequestBody CommentDto comment,
                                                     Authentication authentication) {
        return ResponseEntity.ok(commentService.editComment(adId, commentId, comment,authentication));
    }

    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }
}
