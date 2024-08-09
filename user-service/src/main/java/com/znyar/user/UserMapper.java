package com.znyar.user;

import com.znyar.userinfo.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final CustomBCryptPasswordEncoder encoder;

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
                .password(encoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
    }

    public void updateUser(User user, UserRequest request) {
        user.setPassword(encoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        UserInfo userInfo = user.getUserInfo();
        userInfo.setFirstName(request.getFirstName());
        userInfo.setLastName(request.getLastName());
        userInfo.setGender(request.getGender());
        userInfo.setPhone(request.getPhoneNumber());
        userInfo.setBirthDate(request.getBirthDate());
        userInfo.setBio(request.getBio());
        userInfo.setAddress(request.getAddress());
    }

}
