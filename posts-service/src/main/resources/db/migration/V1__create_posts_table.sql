create schema if not exists posts_schema;

create table if not exists posts_schema.t_post (
    id bigint primary key,
    c_owner_id bigint not null,
    c_title text,
    c_text text,
    c_created_at timestamp not null,
    c_updated_at timestamp not null
);

create table if not exists posts_schema.t_like (
    id bigint primary key,
    c_sender_id bigint not null,
    post_id bigint references posts_schema.t_post(id),
    file_id bigint references posts_schema.t_file(id),
    check (post_id is not null or file_id is not null)
);

create table if not exists posts_schema.t_file (
    id bigint primary key,
    c_owner_id bigint not null,
    c_title text not null,
    c_text text,
    c_filepath text not null
);

create table if not exists posts_schema.t_post_file (
    post_id bigint not null references posts_schema.t_post(id),
    file_id bigint not null references posts_schema.t_file(id)
);

create sequence if not exists posts_schema.t_post_seq start 1 increment 50;
create sequence if not exists posts_schema.t_like_seq start 1 increment 50;
create sequence if not exists posts_schema.t_file_seq start 1 increment 50;