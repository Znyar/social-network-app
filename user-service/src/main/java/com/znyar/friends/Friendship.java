package com.znyar.friends;

public record Friendship(Long userId, Long friendId, boolean isAccepted) {
}
