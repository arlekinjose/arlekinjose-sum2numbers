package com.devopchallenge.sumtwonumbers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class s2nAuthException extends RuntimeException {

    public s2nAuthException(String message) {
        super(message);
    }
}
