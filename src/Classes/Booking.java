package Classes;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class Booking {
    protected int bookingID;
    protected int userID;
    protected LocalDate date;
    protected String origin;
    protected String destination;
    protected double fare;
    protected int numTickets;
    protected int vehicleID;
    protected Route route;

    // Constructor
    public Booking(int bookingID, int userID, LocalDate date, String origin, String destination, double fare, int numTickets, int vehicleID) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.fare = fare;
        this.numTickets = numTickets;
        this.vehicleID = vehicleID;
        
    }
    
    public Booking(Route route, int numTickets, LocalDate date) {
        this.route = route;
        this.numTickets = numTickets;
        this.date = date;
        this.fare = calculatePrice(route.getDistance(), numTickets);
    }
    
    public Booking() {}

    // Getter and Setter for bookingID
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
    
    public Route getRoute() {
        return route;
    }

    // Getter and Setter for userID
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    // Getter and Setter for date
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate localDate) {
        this.date = localDate;
    }

    // Getter and Setter for origin
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    // Getter and Setter for destination
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    // Getter and Setter for fare
    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    // Getter and Setter for numTickets
    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    // Getter and Setter for vehicleID
    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }
    
    public String getBookingType() {
        return this.getClass().getSimpleName().replace("Booking", "");
    }

    // Abstract method to calculate price, must be implemented in subclasses
    public abstract double calculatePrice(double distance, int numTickets); // Each subclass implements specific pricing

    // Abstract method to save the booking to the database
    public abstract boolean saveToDatabase();
}
