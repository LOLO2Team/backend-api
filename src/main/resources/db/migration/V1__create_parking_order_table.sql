create table parking_order (
    id                  bigint          not null,
    vehicle_number      varchar(8)      not null,
    order_status        varchar(20)     DEFAULT 'pending',
    parking_lot_id bigint               null,
    employee_id bigint                  null,
    primary key(id)
)