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

    public List<Friendship> getAllFriends(Long userId) {
        String sql = "SELECT user_id, friend_id, c_is_accepted " +
                "FROM user_schema.t_friends " +
                "WHERE user_id = ? OR friend_id = ?";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Friendship(
                        rs.getLong("user_id"),
                        rs.getLong("friend_id"),
                        rs.getBoolean("c_is_accepted")),
                userId, userId
        );
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

    public void deleteAllByUserId(Long id) {
        String sql = "DELETE FROM user_schema.t_friends WHERE user_id = ? OR friend_id = ?";
        jdbcTemplate.update(sql, id, id);
    }

    public void deleteUserRequest(Long fromUserId, Long toUserId) {
        String sql = "DELETE FROM user_schema.t_friends WHERE user_id = ? AND friend_id = ? AND c_is_accepted = false";
        jdbcTemplate.update(sql, fromUserId, toUserId);
    }

    public void updateFriendshipStatus(Long fromUserId, Long toUserId, boolean accepted) {
        String sql = "UPDATE user_schema.t_friends SET c_is_accepted = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, accepted, fromUserId, toUserId);
    }

    public void swapRelationship(Long fromUserId, Long toUserId) {
        String sql = "UPDATE user_schema.t_friends SET user_id = ?, friend_id = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, toUserId, fromUserId, fromUserId, toUserId);
    }

}
