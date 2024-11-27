package Classes;

import java.sql.Date;
import java.time.LocalDate;

import databaseControllers.DatabaseHandler;

public class BusBooking extends Booking {
    public String busCompany;
    public int seatNumber;

    // Constructor for BusBooking with parameters
    public BusBooking(int bookingID, int userID, LocalDate date, String origin, String destination, String busCompany, int seatNumber, float fare, int numTickets, int vehicleID) {
        super(bookingID, userID, date, origin, destination, fare, numTickets, vehicleID); // Call the Booking constructor
        this.busCompany = busCompany;
        this.seatNumber = seatNumber;
    }

    // Default constructor for BusBooking
    public BusBooking() {
        super(0, 0, null, null, null, 0, 0, 0); // Call the default Booking constructor
        this.busCompany = "";
        this.seatNumber = 0;
    }
    
    public BusBooking(Route route, int numTickets, LocalDate date) {
		this.route = route;
		this.numTickets = numTickets;
		this.date = date;
	}

    @Override
    public double calculatePrice(double distance, int numTickets) {
        // Calculate price based on distance, number of tickets, or other factors
        return (distance * 4) * numTickets; // Example static price calculation
    }


    // Getters and Setters for new attributes
    public String getBusCompany() {
        return busCompany;
    }

    public void setBusCompany(String busCompany) {
        this.busCompany = busCompany;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
