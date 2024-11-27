package Factory;

import java.time.LocalDate;

import Classes.Booking;
import Classes.BusBooking;
import Classes.FlightBooking;
import Classes.Route;
import Classes.TrainBooking;

public class BookingFactory {

    public static Booking createBooking(String type, Route route, int numTickets, LocalDate date) {
        switch (type) {
            case "Flight":
                return new FlightBooking(route, numTickets, date);
            case "Bus":
                return new BusBooking(route, numTickets, date);
            case "Train":
                return new TrainBooking(route, numTickets, date);
            default:
                throw new IllegalArgumentException("Invalid booking type: " + type);
        }
    }
}
