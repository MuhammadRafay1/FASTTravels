package Classes;

import java.sql.Date;

import application.DatabaseHandler;

public class TrainBooking extends Booking {
    private String trainNumber;
    private String compartment;

    public TrainBooking(int bookingID, int userID, Date date, String origin, String destination, String trainNumber, String compartment) {
        super(bookingID, userID, date, origin, destination);
        this.trainNumber = trainNumber;
        this.compartment = compartment;
    }

    @Override
    public double calculatePrice() {
        // Calculate price based on train and compartment
        return 25.0; // Example static price
    }


    @Override
    public boolean saveToDatabase() {
        return DatabaseHandler.saveTrainBooking(this);
    }
}
