package ru.skypro.homework.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.homework.exception.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<ExceptionDetails> exceptionAdsNotFoundHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("Ads not found!");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDetails> exceptionUserNotFoundHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("User not found!");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ExceptionDetails> exceptionCommentNotFoundHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("Comment not found!");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDetails> exceptionAccessDeniedHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("Not enough right");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
    @ExceptionHandler(ChangePasswordException.class)
    public ResponseEntity<ExceptionDetails> changePasswordHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("passwords do not match");
        return ResponseEntity
                .badRequest()
                .body(notFoundDetails);
    }
}
