package com.znyar.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final String searchFieldName;
    private final String message;

    public UserNotFoundException(String searchFieldName, String message) {
        this.message = message;
        this.searchFieldName = searchFieldName;
    }

}
