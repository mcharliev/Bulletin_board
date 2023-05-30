package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {
    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        return ResponseEntity.ok(new ResponseWrapperAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ads> createAds(
            @RequestPart("image") MultipartFile file,
            @RequestPart("properties") CreateAds createAds) {
        return ResponseEntity.ok(new Ads());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullAds> getAds(@PathVariable Integer id) {
        return ResponseEntity.ok(new FullAds());
    }

    @DeleteMapping("/{id}")
    public void deleteAds(@PathVariable Integer id) {

    }

    @PatchMapping("/{id}")
    public ResponseEntity <CreateAds> updateAds(@PathVariable Integer id,
                               @RequestBody Ads ads) {
        return ResponseEntity.ok(new CreateAds());
    }

    @GetMapping("/me")
    public ResponseEntity <ResponseWrapperAds> getAdsMe() {
        return ResponseEntity.ok(new ResponseWrapperAds());
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity <String> uploadAdsImage(@PathVariable Integer id,
                                 @RequestParam MultipartFile image) {
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity <ResponseWrapperComment> getAdsComments(@PathVariable Integer id) {
        return ResponseEntity.ok(new ResponseWrapperComment());
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity <Comment> createAdsComment(@PathVariable Integer id,
                                    @RequestBody Comment comment) {
        return ResponseEntity.ok(new Comment());
    }

    @DeleteMapping("/{adId}/comments/{commentId}/")
    public void deleteAdsComment(@PathVariable("adId") String adId,
                                 @PathVariable("commentId") Integer commentId) {
    }

    @PatchMapping("/{adId}/comments/{commentId}/")
    public ResponseEntity <Comment> editAdsComment(@PathVariable("adId") String adId,
                                  @PathVariable("commentId") Integer commentId,
                                  @RequestBody Comment Comment) {
        return ResponseEntity.ok(new Comment());
    }
}
