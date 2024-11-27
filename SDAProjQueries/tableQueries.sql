
-- Table for Admin-specific data (if needed for additional fields)
CREATE TABLE Admin (
    userID INT PRIMARY KEY,
    FOREIGN KEY (userID) REFERENCES User(userID)
);
-- Table for Bookings
CREATE TABLE Booking (
    bookingID INT PRIMARY KEY,
    userID INT NOT NULL,
    bookingType ENUM('Bus', 'Train', 'Flight') NOT NULL,  -- Discriminator for Bus, Train, Flight bookings
    bookingDate DATE NOT NULL,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    FOREIGN KEY (userID) REFERENCES User(userID)
);

-- Table for Bus-specific Booking details
CREATE TABLE BusBooking (
    bookingID INT PRIMARY KEY,
    busCompany VARCHAR(100),
    seatNumber INT,
    FOREIGN KEY (bookingID) REFERENCES Booking(bookingID)
);

-- Table for Train-specific Booking details
CREATE TABLE TrainBooking (
    bookingID INT PRIMARY KEY,
    trainNumber VARCHAR(50),
    compartment VARCHAR(50),
    FOREIGN KEY (bookingID) REFERENCES Booking(bookingID)
);

-- Table for Flight-specific Booking details
CREATE TABLE FlightBooking (
    bookingID INT PRIMARY KEY,
    airlineCompany VARCHAR(100),
    seatClass VARCHAR(20),
    seatNumber VARCHAR(10),
    FOREIGN KEY (bookingID) REFERENCES Booking(bookingID)
);

-- Table for Routes
CREATE TABLE Route (
    routeID INT PRIMARY KEY,
    startPoint VARCHAR(100) NOT NULL,
    endPoint VARCHAR(100) NOT NULL,
    distance FLOAT NOT NULL,
    duration VARCHAR(20)
);

set foreign_key_checks = 1;
ALTER TABLE Route MODIFY routeID INT AUTO_INCREMENT;


-- Table for Vehicles
CREATE TABLE Vehicle (
    vehicleID INT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,  -- Example: Bus, Train, Flight
    capacity INT NOT NULL,
    availabilityStatus VARCHAR(20) NOT NULL,
    routeID INT,
    FOREIGN KEY (routeID) REFERENCES Route(routeID)
);

CREATE TABLE RouteVehicle (
    routeID INT NOT NULL,
    vehicleID INT NOT NULL,
    duration VARCHAR(50) NOT NULL, -- Duration specific to the vehicle
    availabilityStatus VARCHAR(50) NOT NULL,
    PRIMARY KEY (routeID, vehicleID),
    FOREIGN KEY (routeID) REFERENCES Route(routeID) ON DELETE CASCADE,
    FOREIGN KEY (vehicleID) REFERENCES Vehicle(vehicleID) ON DELETE CASCADE
);

-- Table for Payment (abstract class)
CREATE TABLE Payment (
    paymentID INT PRIMARY KEY,
    bookingID INT NOT NULL,
    amount FLOAT NOT NULL,
    status ENUM('Unpaid', 'Paid') DEFAULT 'Unpaid',
    paymentType ENUM('Cash', 'Card') NOT NULL,  -- Discriminator for CashPayment and CardPayment
    FOREIGN KEY (bookingID) REFERENCES Booking(bookingID)
);

-- Table for Card-specific Payment details
CREATE TABLE CardPayment (
    paymentID INT PRIMARY KEY,
    cardNumber VARCHAR(20) NOT NULL,
    expiryDate VARCHAR(10) NOT NULL,
    cvv VARCHAR(5) NOT NULL,
    FOREIGN KEY (paymentID) REFERENCES Payment(paymentID)
);

-- Table for Cash Payments (no additional fields required)
-- Cash payments can directly use the Payment table with paymentType set to 'Cash'.

-- Table for Complaints

CREATE TABLE Complaint (
    complaintID INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    vehicleID INT,
    details TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'Submitted',
    FOREIGN KEY (userID) REFERENCES User(userID),
    FOREIGN KEY (vehicleID) REFERENCES Vehicle(vehicleID)
);

CREATE TABLE BookingLog (
    logID INT AUTO_INCREMENT PRIMARY KEY,  -- Unique identifier for each log entry
    bookingID INT NOT NULL,                -- The ID of the booking being logged
    userID INT NOT NULL,                   -- The user who made the booking or updated it
    actionType ENUM('INSERT', 'UPDATE') NOT NULL, -- The type of operation
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, -- When the operation occurred
    FOREIGN KEY (bookingID) REFERENCES Booking(bookingID),
    FOREIGN KEY (userID) REFERENCES User(userID)
);


CREATE TABLE UserPreferences (
    preferenceID INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    dietaryPreference VARCHAR(255),
    isHandicapped BOOLEAN DEFAULT FALSE,
    preferredLanguage VARCHAR(100),
    seatPreference VARCHAR(100),
    FOREIGN KEY (userID) REFERENCES User(userID) ON DELETE CASCADE
);

CREATE TABLE CardPayment (
    cardPaymentID INT AUTO_INCREMENT PRIMARY KEY,
    paymentID INT NOT NULL,
    cardNumber VARCHAR(16) NOT NULL, -- Store encrypted data
    cvv VARCHAR(3) NOT NULL, -- Store encrypted data
    cardHolderName VARCHAR(100) NOT NULL,
    FOREIGN KEY (paymentID) REFERENCES Payment(paymentID) ON DELETE CASCADE
);
create Table CargoBooking (
	cargoBookingID INT auto_increment Primary key,
    userID INT NOT NULL,
    routeID int NOT NULL,
    vehicleID int NOT NULL,
    weight int,
    widht int,
    height int,
    bdate date,
	FOREIGN KEY (userID) REFERENCES User(userID) ON DELETE CASCADE,
	FOREIGN KEY (routeID) REFERENCES route(routeID) ON DELETE CASCADE,
	FOREIGN KEY (vehicleID) REFERENCES vehicle(vehicleID) ON DELETE CASCADE
);
create Table UserWallet (
	userWalletID int auto_increment Primary key,
    userID INT NOT NULL,
    walletAmount int not null,
    FOREIGN KEY (userID) REFERENCES User(userID) on delete cascade
);

CREATE TABLE VehicleCapacity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicleID INT NOT NULL,
    date DATE NOT NULL,
    availableSeats INT NOT NULL,
    CONSTRAINT fk_vehicleCapacity FOREIGN KEY (vehicleID) REFERENCES Vehicle(vehicleID)
);

CREATE TABLE Waitlist (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userID INT NOT NULL,
    vehicleID INT NOT NULL,
    date DATE NOT NULL,
    ticketsRequested INT NOT NULL,
    waitlistPosition INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (userID) REFERENCES User (userID),
    CONSTRAINT fk_vehicleWaitlist FOREIGN KEY (vehicleID) REFERENCES Vehicle (vehicleID)
);

CREATE TABLE Vehicle (
    vehicleID INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,  -- Example: Bus, Train, Flight
    capacity INT NOT NULL,
    availabilityStatus VARCHAR(20) NOT NULL,
    company varchar(20),
    routeID INT,
    FOREIGN KEY (routeID) REFERENCES Route(routeID)
);

