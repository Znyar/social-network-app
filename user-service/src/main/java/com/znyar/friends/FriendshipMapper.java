package com.znyar.friends;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendshipMapper {

    public FriendsResponse toFriendsResponse(List<Friendship> friends, Long userId) {
        return FriendsResponse.builder()
                .friendsIds(friends.stream()
                        .filter(Friendship::isAccepted)
                        .map(f -> userId.equals(f.userId()) ? f.friendId() : f.userId())
                        .toList())
                .followingUsersIds(friends.stream()
                        .filter(f -> !f.isAccepted() && userId.equals(f.userId()))
                        .map(Friendship::friendId)
                        .toList())
                .followedUsersIds(friends.stream()
                        .filter(f -> !f.isAccepted() && userId.equals(f.friendId()))
                        .map(Friendship::userId)
                        .toList())
                .build();
    }

}
