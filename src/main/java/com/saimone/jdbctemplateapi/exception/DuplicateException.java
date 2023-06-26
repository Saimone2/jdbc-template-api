package com.saimone.jdbctemplateapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateException extends RuntimeException{
    DuplicateException(String message) {
        super(message);
    }
}