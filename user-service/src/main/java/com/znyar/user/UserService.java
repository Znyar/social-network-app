package com.znyar.user;

import com.znyar.exception.NonUniqueUserDataException;
import com.znyar.exception.UserNotFoundException;
import com.znyar.friends.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final FriendsRepository friendsRepository;

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toUserResponse)
                .orElseThrow(() -> new UserNotFoundException( "id", "User not found with id " + id));
    }

    public UserResponse createUser(UserRequest request) {
        checkNonUniqueUserData(request, 0L);
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

    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresentOrElse(
                user -> {
                    friendsRepository.deleteAllByUserId(user.getId());
                    userRepository.delete(user);
                },
                () -> {throw new UserNotFoundException("id", "User not found with id " + id);}
        );
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id", "User not found with id " + id));
        checkNonUniqueUserData(request, id);
        mapper.updateUser(user, request);
        return mapper.toUserResponse(userRepository.save(user));
    }

    private void checkNonUniqueUserData(
            UserRequest request,
            Long id
    ) {
        boolean isEmailExists = userRepository.existsByEmailAndIdIsNot(request.getEmail(), id);
        boolean isPhoneNumberExists = userRepository.existsByUserInfo_PhoneAndIdIsNot(request.getPhoneNumber(), id);
        Map<String, String> errors = new HashMap<>();
        if (isEmailExists) {
            errors.put("email", "Email " + request.getEmail() + " already exists");
        }
        if (isPhoneNumberExists) {
            errors.put("phoneNumber", "Phone number " + request.getPhoneNumber() + " already exists");
        }
        if (!errors.isEmpty()) {
            throw new NonUniqueUserDataException(errors);
        }
    }

}
