package com.znyar.friends;

import com.znyar.exception.BadRequestException;
import com.znyar.exception.UserNotFoundException;
import com.znyar.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    public FriendsResponse getAllFriends(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("user-id", "User not found with id " + userId));
        return FriendsResponse.builder()
                .friendsIds(friendsRepository.getUserFriendsIds(userId))
                .followingUsersIds(friendsRepository.getRequestedToUsersIds(userId))
                .followedUsersIds(friendsRepository.getRequestedFromUsersIds(userId))
                .build();
    }

    public void requestFriendship(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new BadRequestException(Map.of("friend-request-error", "User with id " + fromUserId + " cannot request friendship to itself"));
        }
        userRepository.findById(fromUserId).ifPresentOrElse(
                user -> {
                    userRepository.findById(toUserId).orElseThrow(
                            () -> new UserNotFoundException("to-user-id", "User not found with id " + toUserId));
                    if (friendsRepository.isRequested(fromUserId, toUserId) || friendsRepository.isRequested(toUserId, fromUserId)) {
                        throw new BadRequestException(Map.of("friend-request-error", "User with id " + fromUserId + " already requested friendship to user with id " + toUserId));
                    }
                    if (friendsRepository.isRequestAccepted(fromUserId, toUserId)) {
                        throw new BadRequestException(Map.of("friend-request-error", "User with id " + toUserId + " already accepted friendship with user with id " + fromUserId));
                    }
                    if (friendsRepository.isRequestAccepted(toUserId, fromUserId)) {
                        throw new BadRequestException(Map.of("friend-request-error", "User with id " + fromUserId + " already accepted friendship with user with id " + toUserId));
                    }
                    friendsRepository.requestFriendship(fromUserId, toUserId);
                },
                () -> {
                    throw new UserNotFoundException("user-id", "User not found with id " + fromUserId);
                });
    }

    public void acceptFriendship(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new BadRequestException(Map.of("friend-accept-error", "User with id " + fromUserId + " cannot accept friendship with itself"));
        }
        userRepository.findById(fromUserId).ifPresentOrElse(
                user -> {
                    userRepository.findById(toUserId).orElseThrow(
                            () -> new UserNotFoundException("to-user-id", "User not found with id " + toUserId));
                    if (friendsRepository.isRequestAccepted(toUserId, fromUserId) ) {
                        throw new BadRequestException(Map.of("friend-accept-error", "User with id " + fromUserId + " already accepted friendship with user with id " + toUserId));
                    }
                    if (friendsRepository.isRequestAccepted(fromUserId, toUserId)) {
                        throw new BadRequestException(Map.of("friend-accept-error", "User with id " + toUserId + " already accepted friendship with user with id " + fromUserId));
                    }
                    if (!friendsRepository.isRequested(toUserId, fromUserId)) {
                        throw new BadRequestException(Map.of("friend-accept-error", "User with id " + toUserId + " did not request friendship to user with id " + fromUserId));
                    }
                    friendsRepository.acceptFriendship(fromUserId, toUserId);
                }, () -> {
                    throw new UserNotFoundException("user-id", "User not found with id " + fromUserId);
                });
    }

}
