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
    private final FriendshipMapper mapper;

    public FriendsResponse getAllFriends(Long userId) {
        checkUserExists(userId);
        return mapper.toFriendsResponse(friendsRepository.getAllFriends(userId), userId);
    }

    public void requestFriendship(Long fromUserId, Long toUserId) {
        checkUserExists(fromUserId);
        checkUserExists(toUserId);
        if (fromUserId.equals(toUserId)) {
            throw new BadRequestException(Map.of("friend-request-error", "User with id " + fromUserId + " cannot request friendship to itself"));
        }
        if (friendsRepository.isRequested(fromUserId, toUserId)) {
            throw new BadRequestException(Map.of("friend-request-error", "User with id " + fromUserId + " already requested friendship to user with id " + toUserId));
        }
        if (friendsRepository.isRequested(toUserId, fromUserId)) {
            throw new BadRequestException(Map.of("friend-request-error", "User with id " + toUserId + " already requested friendship to user with id " + fromUserId));
        }
        if (friendsRepository.isRequestAccepted(fromUserId, toUserId)) {
            throw new BadRequestException(Map.of("friend-request-error", "User with id " + toUserId + " already accepted friendship with user with id " + fromUserId));
        }
        if (friendsRepository.isRequestAccepted(toUserId, fromUserId)) {
            throw new BadRequestException(Map.of("friend-request-error", "User with id " + fromUserId + " already accepted friendship with user with id " + toUserId));
        }
        friendsRepository.requestFriendship(fromUserId, toUserId);
    }

    public void acceptFriendship(Long fromUserId, Long toUserId) {
        checkUserExists(fromUserId);
        checkUserExists(toUserId);
        if (fromUserId.equals(toUserId)) {
            throw new BadRequestException(Map.of("friend-accept-error", "User with id " + fromUserId + " cannot accept friendship with itself"));
        }
        if (friendsRepository.isRequestAccepted(toUserId, fromUserId) ) {
            throw new BadRequestException(Map.of("friend-accept-error", "User with id " + fromUserId + " already accepted friendship with user with id " + toUserId));
        }
        if (friendsRepository.isRequestAccepted(fromUserId, toUserId)) {
            throw new BadRequestException(Map.of("friend-accept-error", "User with id " + toUserId + " already accepted friendship with user with id " + fromUserId));
        }
        if (!friendsRepository.isRequested(toUserId, fromUserId)) {
            throw new BadRequestException(Map.of("friend-accept-error", "User with id " + toUserId + " did not request friendship to user with id " + fromUserId));
        }
        friendsRepository.updateFriendshipStatus(toUserId, fromUserId, true);
    }

    public void deleteFriendship(Long fromUserId, Long toUserId) {
        checkUserExists(fromUserId);
        checkUserExists(toUserId);
        if (fromUserId.equals(toUserId)) {
            throw new BadRequestException(Map.of("friend-accept-error", "User with id " + fromUserId + " cannot delete friendship with itself"));
        }
        if (friendsRepository.isRequested(toUserId, fromUserId)) {
            throw new BadRequestException(Map.of("delete-friendship-error", "User with id " + toUserId + " is a follower of user with id " + fromUserId));
        }
        if (friendsRepository.isRequested(fromUserId, toUserId)) {
            friendsRepository.deleteUserRequest(fromUserId, toUserId);
            return;
        }
        if (friendsRepository.isRequestAccepted(toUserId, fromUserId)) {
            friendsRepository.updateFriendshipStatus(toUserId, fromUserId, false);
            return;
        }
        if (friendsRepository.isRequestAccepted(fromUserId, toUserId)) {
            friendsRepository.swapRelationship(fromUserId, toUserId);
            friendsRepository.updateFriendshipStatus(toUserId, fromUserId, false);
            return;
        }
        throw new BadRequestException(Map.of("delete-friendship-error", "No friendship between users with id " + fromUserId + " and " + toUserId));
    }

    private void checkUserExists(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user-id", "User not found with id " + userId));
    }

}
