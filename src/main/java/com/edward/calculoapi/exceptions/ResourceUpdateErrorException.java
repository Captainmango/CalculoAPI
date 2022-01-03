package com.edward.calculoapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceUpdateErrorException extends RuntimeException {
    public ResourceUpdateErrorException(String message) {
        super(message);
    }
}
