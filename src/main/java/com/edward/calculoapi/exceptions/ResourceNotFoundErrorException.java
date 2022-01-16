package com.edward.calculoapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundErrorException extends RuntimeException {
    public ResourceNotFoundErrorException(String message) {
        super(message);
    }
}
