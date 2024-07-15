create schema if not exists token_schema;

create table if not exists token_schema.t_token (

    id bigint primary key not null,
    c_type varchar(20) not null,
    c_token varchar(255) not null,
    c_expired boolean not null,
    c_revoked boolean not null,
    c_user_id bigint not null

);

create sequence if not exists token_schema.t_token_seq start 1 increment 50;


