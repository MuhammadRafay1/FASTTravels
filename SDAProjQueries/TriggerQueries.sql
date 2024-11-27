DELIMITER //

CREATE TRIGGER validate_user_before_insert
BEFORE INSERT ON User
FOR EACH ROW
BEGIN
    -- Validate email format
    IF NEW.email NOT LIKE '%_@__%.__%' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid email format';
    END IF;

    -- Validate CNIC length (13 characters)
    IF CHAR_LENGTH(NEW.cnic) != 13 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'CNIC must be 13 characters long';
    END IF;

    -- Validate phone number (numeric only)
    IF NEW.phone REGEXP '[^0-9]' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Phone number must contain only digits';
    END IF;
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER validate_booking_before_insert
BEFORE INSERT ON Booking
FOR EACH ROW
BEGIN
    -- Validate origin and destination are not the same
    IF NEW.origin = NEW.destination THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Origin and destination cannot be the same';
    END IF;

    -- Validate booking date is in the future
    IF NEW.bookingDate <= CURRENT_DATE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Booking date must be in the future';
    END IF;
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER validate_vehicle_before_insert
BEFORE INSERT ON Vehicle
FOR EACH ROW
BEGIN
    -- Validate capacity is greater than 0
    IF NEW.capacity <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Vehicle capacity must be greater than 0';
    END IF;

    -- Validate availability status
    IF NEW.availabilityStatus NOT IN ('Available', 'Unavailable') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid availability status. Must be Available or Unavailable';
    END IF;
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER validate_payment_before_insert
BEFORE INSERT ON Payment
FOR EACH ROW
BEGIN
    -- Validate amount is greater than 0
    IF NEW.amount <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Payment amount must be greater than 0';
    END IF;

    -- Validate status
    IF NEW.status NOT IN ('Paid', 'Unpaid') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid payment status. Must be Paid or Unpaid';
    END IF;
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER validate_complaint_before_insert
BEFORE INSERT ON Complaint
FOR EACH ROW
BEGIN
    -- Validate complaint details are not empty
    IF NEW.details = '' OR NEW.details IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Complaint details cannot be empty';
    END IF;

    -- Validate complaint status
    IF NEW.status NOT IN ('Submitted', 'In Progress', 'Resolved') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid complaint status. Must be Submitted, In Progress, or Resolved';
    END IF;
END;
//

DELIMITER ;


DELIMITER //

CREATE TRIGGER validate_route_before_insert
BEFORE INSERT ON Route
FOR EACH ROW
BEGIN
    -- Validate distance is greater than 0
    IF NEW.distance <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Distance must be greater than 0';
    END IF;

END;
//

DELIMITER ;
ALTER TABLE booking
MODIFY bookingID INT AUTO_INCREMENT;

SET foreign_key_checks = 1;
DELIMITER //

CREATE TRIGGER update_loyalty_points_after_booking
AFTER INSERT ON Booking
FOR EACH ROW
BEGIN
    IF NEW.bookingType = 'Flight' THEN
        UPDATE CustomerUser 
        SET loyaltyPoints = loyaltyPoints + 50
        WHERE userID = NEW.userID;
    ELSEIF NEW.bookingType = 'Train' THEN
        UPDATE CustomerUser 
        SET loyaltyPoints = loyaltyPoints + 30
        WHERE userID = NEW.userID;
    ELSEIF NEW.bookingType = 'Bus' THEN
        UPDATE CustomerUser 
        SET loyaltyPoints = loyaltyPoints + 10
        WHERE userID = NEW.userID;
    END IF;
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER delete_bookings_when_user_deleted
BEFORE DELETE ON User
FOR EACH ROW
BEGIN
    DELETE FROM Booking WHERE userID = OLD.userID;
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER prevent_duplicate_complaints
BEFORE INSERT ON Complaint
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM Complaint
        WHERE userID = NEW.userID
        AND vehicleID = NEW.vehicleID
        AND details = NEW.details
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Duplicate complaint detected';
    END IF;
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER log_booking_insert
AFTER INSERT ON Booking
FOR EACH ROW
BEGIN
    -- Insert log into BookingLog for INSERT operation
    INSERT INTO BookingLog (bookingID, userID, actionType)
    VALUES (NEW.bookingID, NEW.userID, 'INSERT');
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER log_booking_update
AFTER UPDATE ON Booking
FOR EACH ROW
BEGIN
    -- Insert log into BookingLog for UPDATE operation
    INSERT INTO BookingLog (bookingID, userID, actionType)
    VALUES (NEW.bookingID, NEW.userID, 'UPDATE');
END;
//

DELIMITER ;

DELIMITER //

CREATE TRIGGER update_user_type_on_admin_insert
AFTER INSERT ON Admin
FOR EACH ROW
BEGIN
    -- Update the userType in the User table to 'Admin' for the corresponding userID
    UPDATE User
    SET userType = 'Admin'
    WHERE userID = NEW.userID;
END;
//

DELIMITER ;









