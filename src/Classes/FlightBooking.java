package Classes;

import java.sql.Date;
import java.time.LocalDate;

import databaseControllers.DatabaseHandler;

public class FlightBooking extends Booking {
    private String airlineCompany;
    private String seatClass;
    private String seatNumber;

    // Constructor for FlightBooking with parameters
    public FlightBooking(int bookingID, int userID, LocalDate date, String origin, String destination, String airline, String seatClass, String seatNumber, float fare, int numTickets, int vehicleID) {
        super(bookingID, userID, date, origin, destination, fare, numTickets, vehicleID); // Call the Booking constructor
        this.airlineCompany = airline;
        this.seatClass = seatClass;
        this.seatNumber = seatNumber;
    }

    // Default constructor for FlightBooking
    public FlightBooking() {
        super(0, 0, null, null, null, 0, 0, 0); // Call the default Booking constructor
        // Default values can be set if necessary (e.g., airlineCompany, seatClass, seatNumber)
        this.airlineCompany = "";
        this.seatClass = "";
        this.seatNumber = "";
    }

    public FlightBooking(Route route, int numTickets, LocalDate date) {
		this.route = route;
		this.numTickets = numTickets;
		this.date = date;
	}

	@Override
    public double calculatePrice(double distance, int numTickets) {
        // Calculate price based on distance, number of tickets, or other factors
        return (distance * 10) * numTickets; // Example static price
    }


    // Getters and Setters for new attributes
    public String getAirlineCompany() {
        return airlineCompany;
    }

    public void setAirlineCompany(String airlineCompany) {
        this.airlineCompany = airlineCompany;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
