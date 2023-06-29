-- liquibase formatted sql
-- changeset mcharliev:1
create table user_info
(
    id         serial primary key,
    email      varchar(255),
    first_name varchar(255),
    image      varchar(255),
    password   varchar(255),
    role       varchar(255),
    last_name  varchar(255),
    phone      varchar(255)
);

create table IF NOT EXISTS ads_info
(
    id serial primary key,
    description varchar(255),
    image     varchar(255),
    price     integer,
    title     varchar(255),
    author_id integer
);


create table IF NOT EXISTS comment_info
(
    id serial  primary key,
    created_at timestamp,
    text varchar(255),
    ads_id    integer,
    author_id integer
);

create table image_info
(
    id         varchar(255) primary key,
    image      bytea
);






