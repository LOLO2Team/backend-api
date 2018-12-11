create table users (
    id              bigint          not null,
    username     varchar(32)     not null,
    password     varchar(200)     not null,
    unique(username)
)