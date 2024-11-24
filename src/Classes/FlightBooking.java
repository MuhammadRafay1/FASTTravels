package Classes;


import java.sql.Date;

import application.DatabaseHandler;

public class FlightBooking extends Booking {
    private String airlineCompany;
    private String seatClass;
    private String seatNumber;

    public FlightBooking(int bookingID, int userID, Date date, String origin, String destination, String airline, String seatClass, String seatNumber) {
        super(bookingID, userID, date, origin, destination);
        this.airlineCompany = airline;
        this.seatClass = seatClass;
        this.seatNumber = seatNumber;
    }

    @Override
    public double calculatePrice() {
        // Calculate price based on airline and seat class
        return 150.0; // Example static price
    }

    @Override
    public boolean saveToDatabase() {
        return DatabaseHandler.saveFlightBooking(this);
    }
}

