package com.znyar.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendsRepository {

    private final JdbcTemplate jdbcTemplate;

    public void requestFriendship(Long fromUserId, Long toUserId) {
        String sql = "INSERT INTO user_schema.t_friends (user_id, friend_id, c_is_accepted) VALUES (?, ?, false)";
        jdbcTemplate.update(sql, fromUserId, toUserId);
    }

    public void acceptFriendship(Long fromUserId, Long toUserId) {
        String sql = "UPDATE user_schema.t_friends SET c_is_accepted = true WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, toUserId, fromUserId);
    }

    public List<Long> getUserFriendsIds(Long userId) {
        String sql = "SELECT friend_id FROM user_schema.t_friends WHERE user_id = ? AND c_is_accepted = true " +
                "UNION " +
                "SELECT user_id FROM user_schema.t_friends WHERE friend_id = ? AND c_is_accepted = true";
        return jdbcTemplate.queryForList(sql, Long.class, userId, userId);
    }
    
    public List<Long> getRequestedToUsersIds(Long userId) {
        String sql = "SELECT friend_id FROM user_schema.t_friends WHERE user_id = ? AND c_is_accepted = false";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    public List<Long> getRequestedFromUsersIds(Long userId) {
        String sql = "SELECT user_id FROM user_schema.t_friends WHERE friend_id = ? AND c_is_accepted = false";
        return jdbcTemplate.queryForList(sql, Long.class, userId);
    }

    public boolean isRequestAccepted(Long fromUserId, Long toUserId) {
        String sql = "SELECT COUNT(*) FROM user_schema.t_friends " +
                "WHERE user_id = ? AND friend_id = ? AND c_is_accepted = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, fromUserId, toUserId);
        return count != null && count > 0;
    }

    public boolean isRequested(Long fromUserId, Long toUserId) {
        String sql = "SELECT COUNT(*) FROM user_schema.t_friends " +
                "WHERE user_id = ? AND friend_id = ? AND c_is_accepted = false";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, fromUserId, toUserId);
        return count != null && count > 0;
    }

}
