package com.znyar.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{user-id}/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping
    public ResponseEntity<FriendsResponse> getAllFriends(
            @PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(friendsService.getAllFriends(userId));
    }

    @PostMapping("/request")
    public ResponseEntity<Void> requestFriendship(
            @PathVariable("user-id") Long fromUserId,
            @RequestParam("to-user-id") Long toUserId
    ) {
        friendsService.requestFriendship(fromUserId, toUserId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendship(
            @PathVariable("user-id") Long fromUserId,
            @RequestParam("to-user-id") Long toUserId
    ) {
        friendsService.acceptFriendship(fromUserId, toUserId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFriendship(
            @PathVariable("user-id") Long fromUserId,
            @RequestParam("to-user-id") Long toUserId
    ) {
        friendsService.deleteFriendship(fromUserId, toUserId);
        return ResponseEntity.accepted().build();
    }

}
