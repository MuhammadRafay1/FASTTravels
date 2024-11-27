package Classes;

import databaseControllers.BookingDatabaseHandler;

import java.sql.SQLException;

public abstract class Payment {
    protected int paymentID;
    protected int bookingID;
    protected float amount;
    private String status;

    private static final BookingDatabaseHandler dbHandler = new BookingDatabaseHandler();

    public Payment(int paymentID, int bookingID, float amount) {
        this.paymentID = paymentID;
        this.bookingID = bookingID;
        this.amount = amount;
        this.status = "Unpaid";
    }

    public boolean processPayment() {
        this.status = "Paid";
        System.out.println("Payment processed.");
        return true;
    }

    // Save payment record in the database
    public void savePayment(String type) throws SQLException {
        dbHandler.insertingToPayment(bookingID, amount);
    }

    // Load payment details using the booking ID
    public static float getFareByBookingID(int bookingID) throws SQLException {
        return dbHandler.loadBookingDetailsWithBookingID(bookingID);
    }

    // Other common payment logic can be added here
}


