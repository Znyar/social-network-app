package com.znyar.exception;

import lombok.Getter;

@Getter
public class AuthServiceProcessingException extends RuntimeException {

    public AuthServiceProcessingException(String message) {
        super(message);
    }

}
