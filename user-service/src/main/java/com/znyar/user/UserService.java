package com.znyar.user;

import com.znyar.exception.NonUniqueUserDataException;
import com.znyar.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException( "id", "User not found with id " + id));
    }

    public UserResponse createUser(UserRequest request) {
        boolean isEmailUnique = userRepository.existsByEmail(request.getEmail());
        boolean isPhoneNumberExists = userRepository.existsByUserInfo_Phone(request.getPhoneNumber());
        Map<String, String> errors = new HashMap<>();
        if (isEmailUnique) {
            errors.put("email", "Email " + request.getEmail() + " already exists");
        }
        if (isPhoneNumberExists) {
            errors.put("phoneNumber", "Phone number " + request.getPhoneNumber() + " already exists");
        }
        if (!errors.isEmpty()) {
            throw new NonUniqueUserDataException(errors);
        }
        return mapper.toUserResponse(
                userRepository.save(
                        mapper.toUser(request)
                )
        );
    }

    public UserResponse getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException("email", "User not found with email " + email));
    }

    //TODO update user with password

}
