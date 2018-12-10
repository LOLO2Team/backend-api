INSERT INTO parking_lot (id, capacity, reserved_space) VALUES (0, 50, 0);
INSERT INTO parking_boy (id, name) VALUES (0, 'Locki');

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (99994, 12345678, 'pending', null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (99995, 13869403, 'pending', null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (99996, 35804146, 'pending', null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (99997, 56383021, 'parking', 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (99998, 35466842, 'parking', 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (99999, 12367764, 'fetched', 0);