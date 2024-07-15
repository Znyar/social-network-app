create schema if not exists user_schema;

create table if not exists user_schema.t_user_info (

    id bigint primary key not null,
    c_firstname varchar(255) not null,
    c_lastname varchar(255) not null,
    c_address varchar(255),
    c_phone varchar(255) unique,
    c_birth_date date,
    c_gender varchar(10),
    c_bio text,
    c_created_at TIMESTAMP not null,
    c_updated_at TIMESTAMP not null

);

create table if not exists user_schema.t_user (

    id bigint primary key not null,
    c_password varchar(255) not null,
    c_email varchar(100) not null unique,
    c_created_at TIMESTAMP not null,
    c_updated_at TIMESTAMP not null,
    user_info_id bigint references user_schema.t_user_info(id) on delete cascade not null

);

create sequence if not exists user_schema.t_user_info_seq start 1 increment 50;
create sequence if not exists user_schema.t_user_seq start 1 increment 50;