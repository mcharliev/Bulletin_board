package ru.skypro.homework.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skypro.homework.exception.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionAdsNotFoundHandler() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Ads not found!");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionUserNotFoundHandler() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("User not found!");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorDetails> exceptionCommentNotFoundHandler() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setMessage("Comment not found!");
        return ResponseEntity
                .badRequest()
                .body(errorDetails);
    }
}
