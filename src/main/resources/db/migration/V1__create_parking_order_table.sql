create table parking_order (
    id                  bigint          not null,
    vehicle_number      varchar(8)      not null,
    order_status        varchar(20)     ,
    primary key(id)
)