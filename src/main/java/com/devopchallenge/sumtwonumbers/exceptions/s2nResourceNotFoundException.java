package com.devopchallenge.sumtwonumbers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class s2nResourceNotFoundException extends RuntimeException {

    public s2nResourceNotFoundException(String message) {
        super(message);
    }
}
