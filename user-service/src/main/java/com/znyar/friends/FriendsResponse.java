package com.znyar.friends;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FriendsResponse {

    private List<Long> friendsIds;
    private List<Long> followingUsersIds;
    private List<Long> followedUsersIds;

}
