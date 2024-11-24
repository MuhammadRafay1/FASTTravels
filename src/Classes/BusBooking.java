package Classes;
import java.sql.Date;

import application.DatabaseHandler;

public class BusBooking extends Booking {
    public String busCompany;
    public int seatNumber;

    public BusBooking(int bookingID, int userID, Date date, String origin, String destination, String busCompany, int seatNumber) {
        super(bookingID, userID, date, origin, destination);
        this.busCompany = busCompany;
        this.seatNumber = seatNumber;
    }

    @Override
    public double calculatePrice() {
        // Calculate price based on bus company or other factors
        return 15.0; // Example static price
    }

	@Override
	public boolean saveToDatabase() {
		// TODO Auto-generated method stub
		return false;
	}

//    @Override
////    public boolean saveToDatabase() {
////        return DatabaseHandler.saveBusBooking(this);
////    }
}
