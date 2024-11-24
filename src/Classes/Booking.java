package Classes;


import java.io.IOException;
import java.sql.Date;

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
    protected Date date;
    protected String origin;
    protected String destination;

    public Booking(int bookingID, int userID, Date date, String origin, String destination) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
    }

    public int getBookingID() {
        return bookingID;
    }

    public int getUserID() {
        return userID;
    }

    public Date getDate() {
        return date;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public abstract double calculatePrice(); // Each subclass implements specific pricing

    public abstract boolean saveToDatabase();
    

}
