package com.edward.calculoapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ResourceNotSavedException extends RuntimeException
{
    public ResourceNotSavedException(String message) {
        super(message);
    }
}
