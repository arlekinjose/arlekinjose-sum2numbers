package com.devopchallenge.sumtwonumbers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class s2nBadRequestException extends RuntimeException {

    public s2nBadRequestException(String message) {
        super(message);
    }
}
