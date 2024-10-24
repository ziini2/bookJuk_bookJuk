package com.itwillbs.bookjuk.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        //스택트레이스 비활성화
        super(message, null, false, false);
    }
}
