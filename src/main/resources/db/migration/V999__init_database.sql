INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (0, 'Locki', 'locki999', 'locki@mail.com', 87562153, '$2a$10$DJIH.TaCpm/ILwW9yf937OZl2KvOCFOzkaSDScYGfH5cQA7kOPlFG','WORKING');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9994, 'Rex', 'rexlo', 'rexxx@mail.com', 68541257, '$2a$10$DJIH.TaCpm/ILwW9yf937OZl2KvOCFOzkaSDScYGfH5cQA7kOPlFG','WORKING');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9995, 'Jacky', 'jackyboy', 'jk@ma.com', 12357986, '$2a$10$DJIH.TaCpm/ILwW9yf937OZl2KvOCFOzkaSDScYGfH5cQA7kOPlFG', 'WORKING');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9996, 'Kate', 'kate', 'k@mail.com', 98563201, '$2a$10$DJIH.TaCpm/ILwW9yf937OZl2KvOCFOzkaSDScYGfH5cQA7kOPlFG', 'FROZEN');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9997, 'Mo', 'mo', 'mo@mail.com', 95632501, '$2a$10$DJIH.TaCpm/ILwW9yf937OZl2KvOCFOzkaSDScYGfH5cQA7kOPlFG', 'FROZEN');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9998, 'admin', 'admin', 'admin@mail.com', 51245879, '$2a$10$DJIH.TaCpm/ILwW9yf937OZl2KvOCFOzkaSDScYGfH5cQA7kOPlFG', 'WORKING');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (9999, 'human', 'human', 'human@mail.com', 15423568, '$2a$10$DJIH.TaCpm/ILwW9yf937OZl2KvOCFOzkaSDScYGfH5cQA7kOPlFG', 'WORKING');
INSERT INTO employee (id, name, username, email, phone, password, status) VALUES (10000, 'manager', 'manager', 'manager@mail.com', 15268798, '$2a$10$DJIH.TaCpm/ILwW9yf937OZl2KvOCFOzkaSDScYGfH5cQA7kOPlFG', 'WORKING');


INSERT INTO employee_authority (employee_id, authority_id)VALUES(0, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9994, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9995, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9996, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9997, 9992);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9998, 9999);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(9999, 9994);
INSERT INTO employee_authority (employee_id, authority_id)VALUES(10000, 9993);

INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (0,'Science Parking Lot', 50, 0, 0);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9001,'Ocean Parking Area', 10, 0, 0);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9002,'Software Parking Space', 70, 0, 9994);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9003,'Grand ParkingLot', 150, 0, 0);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9004,'Almost Full ParkingLot', 1, 0, 0);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9005,'Grand ParkingLot', 10, 0, null);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9006,'Old Grand LOT', 5, 0, null);
INSERT INTO parking_lot (id, parking_lot_name, capacity, reserved_space, employee_id) VALUES (9007,'New Lot besides Sea', 5, 0, null);

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99996, 35804146, 'parking', null, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99997, 56383021, 'parked', 0, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99998, 35466842, 'fetching', 0, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99999, 12367764, 'fetched', 0, 0);

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99912, 45638921, 'parking', null, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99913, 34636789, 'parking', null, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99914, 23474257, 'parking', null, 9994);

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99100, 'PROCAR', 'pending', null, null);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99101, 'HELLO', 'pending', null, null);

INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99202, 'HFIA', 'parked', 0, 0);
INSERT INTO parking_order (id, vehicle_number, order_status, parking_lot_id, employee_id) VALUES (99203, 'INFRA', 'parked', 0, 0);