package com.example.exception;

import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends RuntimeException implements BaseHttpException{
    public TokenNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
