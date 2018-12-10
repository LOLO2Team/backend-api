INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (0,'Grand ParkingLot', 150, 0, 0);
INSERT INTO parking_boy (id, name) VALUES (0, 'Locki');

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99994, 12345678, 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99995, 13869403, 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99996, 35804146, 'parking', null, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99997, 56383021, 'parked', 0, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99998, 35466842, 'fetching', 0, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99999, 12367764, 'fetched', 0, 0);