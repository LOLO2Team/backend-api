create table parking_lot (
    id                    bigint  not null,
    parking_lot_name      varchar(64) not null,
    capacity              int  not null,
    reserved_space        int  null,
    employee_id bigint    null,
    parking_lot_status    VARCHAR not null DEFAULT 'OPEN',
    primary key (id)
)