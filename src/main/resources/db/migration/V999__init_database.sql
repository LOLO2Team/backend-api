INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (0, 'Locki', 'locki999', 'locki@mail.com', 87562153, '123','WORKING');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9994, 'Rex', 'rexlo', 'rexxx@mail.com', 68541257, 'password','WORKING');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9995, 'Jacky', 'jackyboy', 'jk@ma.com', 12357986, 'password', 'WORKING');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9996, 'Kate', 'kate', 'k@mail.com', 98563201, 'password', 'FROZEN');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9997, 'Mo', 'mo', 'mo@mail.com', 95632501, 'password', 'FROZEN');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9998, 'admin', 'admin', 'admin@mail.com', 51245879, 'admin', 'WORKING');

INSERT INTO employee_authority (employee_id, authority_id)VALUES(0, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9994, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9995, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9996, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9997, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9998, 1);

INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (0,'Science Parking Lot', 50, 0, 0);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9001,'Ocean Parking Area', 10, 0, 0);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9002,'Software Parking Space', 70, 0, 9994);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9003,'Grand ParkingLot', 150, 0, 0);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9004,'Almost Full ParkingLot', 1, 0, 0);

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99994, 12345678, 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99995, 13869403, 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99996, 35804146, 'parking', null, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99997, 56383021, 'parked', 0, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99998, 35466842, 'fetching', 0, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99999, 12367764, 'fetched', 0, 0);

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99912, 45638921, 'parking', 0, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99913, 34636789, 'parking', 9001, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99914, 23474257, 'parking', 9002, 9994);

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99100, 'PROCAR', 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99101, 'HELLO', 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99102, 'DEMO', 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99103, 'IAMACAR', 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99104, 'HEMIN', 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99105, 'LOCA', 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99106, '135AAA', 'pending', null, null);