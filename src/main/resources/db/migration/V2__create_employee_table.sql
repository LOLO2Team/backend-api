create table employee (
    id              bigint          not null,
    name            varchar         not null,
    username        varchar         not null,
    email           varchar         not null,
    phone           varchar         not null,
    password        varchar         not null,
    role            varchar         not null,
    primary key (id),
    unique(username),
    unique(email),
    unique(phone)
)