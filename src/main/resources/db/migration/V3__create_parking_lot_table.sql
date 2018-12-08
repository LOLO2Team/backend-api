create table parking_lot (
    id                    bigint  not null,
    capacity              int  not null,
    reserved_space        int  null,
    primary key (id)
)