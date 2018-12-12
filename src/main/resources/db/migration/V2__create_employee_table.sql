create table employee (
    id              bigint          NOT NULL,
    name            varchar         NOT NULL,
    username        varchar         NOT NULL,
    email           varchar         NOT NULL,
    phone           varchar         NOT NULL,
    password        varchar         NOT NULL,
    role            varchar         NOT NULL,
    status          varchar         NOT NULL DEFAULT 'WORKING',
    primary key (id),
    unique(username),
    unique(email),
    unique(phone)
)