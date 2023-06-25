package ru.skypro.homework.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.homework.exception.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<NotFoundDetails> exceptionAdsNotFoundHandler() {
        NotFoundDetails notFoundDetails = new NotFoundDetails();
        notFoundDetails.setMessage("Ads not found!");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<NotFoundDetails> exceptionUserNotFoundHandler() {
        NotFoundDetails notFoundDetails = new NotFoundDetails();
        notFoundDetails.setMessage("User not found!");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<NotFoundDetails> exceptionCommentNotFoundHandler() {
        NotFoundDetails notFoundDetails = new NotFoundDetails();
        notFoundDetails.setMessage("Comment not found!");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<NotFoundDetails> exceptionAccessDeniedHandler() {
        NotFoundDetails notFoundDetails = new NotFoundDetails();
        notFoundDetails.setMessage("Not enough right");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
}
