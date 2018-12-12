create table users (
    id              bigint          not null,
    name            varchar(32)     not null,
    username        varchar(32)     not null,
    password        varchar(200)     not null,
    primary key(id), unique(username)
)