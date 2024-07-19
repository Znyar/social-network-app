package com.znyar.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Data
@JsonInclude(NON_NULL)
public class User {

    private Long id;
    private String email;
    private String password;

}
