package com.znyar.user;

import com.znyar.userinfo.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserResponse {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String bio;
    private String phoneNumber;
    private String address;

}
