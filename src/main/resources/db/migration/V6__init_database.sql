INSERT INTO parking_lot (id, capacity, reserved_space) VALUES (0, 50, 0);
INSERT INTO parking_boy (id, name) VALUES (0, 'Locki');

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (0, 12345678, 'pending', null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (1, 13869403, 'pending', null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (2, 35804146, 'pending', null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (3, 56383021, 'parking', 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (4, 35466842, 'parking', 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id) VALUES (5, 12367764, 'fetched', 0);