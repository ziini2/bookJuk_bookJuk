package com.itwillbs.bookjuk.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //validationException 처리부분
    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(ValidationException exception) {
        log.error("유효성 검사 실페: {}", exception.getMessage());
    }

    //모든 예외에 대한 일반 처리(스택트레이스 제외)
    @ExceptionHandler(Exception.class)
    public void handleException(Exception exception) {
        log.error("서버 오류 발생: {}", exception.getMessage());
    }
}
