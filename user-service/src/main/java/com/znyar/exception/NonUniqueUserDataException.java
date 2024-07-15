package com.znyar.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class NonUniqueUserDataException extends RuntimeException {

    private final Map<String, String> errors;

    public NonUniqueUserDataException(Map<String, String> errors) {
        this.errors = errors;
    }

}
