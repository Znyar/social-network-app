package com.znyar.user;

import com.znyar.userinfo.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getUserInfo().getLastName())
                .lastName(user.getUserInfo().getLastName())
                .bio(user.getUserInfo().getBio())
                .address(user.getUserInfo().getAddress())
                .phoneNumber(user.getUserInfo().getPhone())
                .birthDate(user.getUserInfo().getBirthDate())
                .gender(user.getUserInfo().getGender())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }

    public User toUser(UserRequest request) {
        return User.builder()
                .userInfo(UserInfo.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .gender(request.getGender())
                        .phone(request.getPhoneNumber())
                        .bio(request.getBio())
                        .birthDate(request.getBirthDate())
                        .address(request.getAddress())
                        .build())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();
    }
}
