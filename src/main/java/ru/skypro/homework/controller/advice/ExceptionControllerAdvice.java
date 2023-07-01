package ru.skypro.homework.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.homework.exception.*;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<ExceptionDetails> exceptionAdsNotFoundHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("Ads not found!");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(notFoundDetails);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDetails> exceptionUserNotFoundHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("User not found!");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(notFoundDetails);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ExceptionDetails> exceptionCommentNotFoundHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("Comment not found!");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(notFoundDetails);
    }
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ExceptionDetails> ImageNotFoundHandler() {
        ExceptionDetails notFoundDetails = new ExceptionDetails();
        notFoundDetails.setMessage("image not found");
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(notFoundDetails);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDetails> exceptionAccessDeniedHandler() {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage("Not enough right");
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(exceptionDetails);
    }

    @ExceptionHandler(ChangePasswordException.class)
    public ResponseEntity<ExceptionDetails> changePasswordHandler() {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage("password do not match");
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(exceptionDetails);
    }
}
