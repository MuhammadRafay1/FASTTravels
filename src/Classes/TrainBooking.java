package Classes;

import java.sql.Date;
import java.time.LocalDate;

import databaseControllers.DatabaseHandler;

public class TrainBooking extends Booking {
    private String trainNumber;
    private String compartment;

    // Constructor for TrainBooking with parameters
    public TrainBooking(int bookingID, int userID, LocalDate date, String origin, String destination, String trainNumber, String compartment, float fare, int numTickets, int vehicleID) {
        super(bookingID, userID, date, origin, destination, fare, numTickets, vehicleID); // Call the Booking constructor
        this.trainNumber = trainNumber;
        this.compartment = compartment;
    }

    // Default constructor for TrainBooking
    public TrainBooking() {
        super(0, 0, null, null, null, 0, 0, 0); // Call the default Booking constructor
        this.trainNumber = "";
        this.compartment = "";
    }
    
    public TrainBooking(Route route, int numTickets, LocalDate date) {
		this.route = route;
		this.numTickets = numTickets;
		this.date = date;
	}

    @Override
    public double calculatePrice(double distance, int numTickets) {
        // Calculate price based on distance, number of tickets, or other factors
        return (distance * 2) * numTickets; // Example static price calculation
    }

    @Override
    public boolean saveToDatabase() {
        // Implement saving logic (this is just a placeholder for now)
        return false; // Assuming you have a method to handle saving train bookings
    }

    // Getters and Setters for new attributes
    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getCompartment() {
        return compartment;
    }

    public void setCompartment(String compartment) {
        this.compartment = compartment;
    }
}
