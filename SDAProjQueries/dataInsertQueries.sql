INSERT INTO User (userID, name, email, password, cnic, phone)
VALUES
(1, 'Alice', 'alice@example.com', 'pass123', '1234567890123', '03012345678'),
(2, 'Bob', 'bob@example.com', 'pass456', '2345678901234', '03112345678'),
(3, 'Charlie', 'charlie@example.com', 'pass789', '3456789012345', '03212345678'),
(4, 'David', 'david@example.com', 'pass321', '4567890123456', '03312345678'),
(5, 'Eve', 'eve@example.com', 'pass654', '5678901234567', '03412345678');

INSERT INTO Admin (userID)
VALUES
(1);

INSERT INTO CustomerUser (userID, loyaltyPoints)
VALUES
(2, 50),
(3, 10),
(4, 30),
(5, 0);

INSERT INTO Route (routeID, startPoint, endPoint, distance)
VALUES
(1, 'Karachi', 'Islamabad', 1200.0),
(2, 'Karachi', 'Hyderabad', 200.0),
(3, 'Lahore', 'Islamabad', 300.0),
(4, 'Lahore', 'Karachi', 1000.0),
(5, 'Karachi', 'Lahore', 1000.0);

INSERT INTO Vehicle (vehicleID, type, capacity, availabilityStatus, routeID,company)
VALUES
(1, 'Bus', 50, 'Available', 1, 'FM Movers'),
(2, 'Bus', 40, 'Available', 2, 'Daewoo'),
(3, 'Bus', 45, 'Unavailable', 3, 'Daewoo'),
(4, 'Train', 180, 'Available', 4,'Green Line'),
(5, 'Flight', 250, 'Available', 5, 'Fly Jinnah'),
(6, 'Flight', 250, 'Available', 1, 'PIA'),
(7, 'Flight', 200, 'Available', 4, 'Fly Jinnah');






