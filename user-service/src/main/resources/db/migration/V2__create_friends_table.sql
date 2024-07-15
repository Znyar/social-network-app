CREATE TABLE user_schema.t_friends (
    user_id bigint not null references user_schema.t_user(id),
    friend_id bigint not null references user_schema.t_user(id) check (user_id != friend_id),
    c_is_accepted boolean not null,
    UNIQUE (user_id, friend_id)
);