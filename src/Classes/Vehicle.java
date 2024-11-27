package Classes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import databaseControllers.VehicleRouteDatabaseHandler;

public class Vehicle {
    private int vehicleID;
    private String type;
    private int capacity;
    private String availabilityStatus;
    private int routeID;
    private String company;
    private static final VehicleRouteDatabaseHandler dbHandler = new VehicleRouteDatabaseHandler();

    // Constructor with parameters
    public Vehicle(int vehicleID, String type, int capacity, String availabilityStatus, String company) {
        this.vehicleID = vehicleID;
        this.type = type;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
        this.company = company;
    }
    
    // Default constructor
    public Vehicle() {}

    // Constructor with vehicleID and company (used for some specific cases)
    public Vehicle(int vehicleID, String company) {
        this.vehicleID = vehicleID;
        this.company = company;
    }

    // Getter and Setter for vehicleID
    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    // Getter and Setter for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter and Setter for capacity
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Getter and Setter for availabilityStatus
    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    // Getter and Setter for routeID
    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    // Getter and Setter for company
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    
    // Override toString() method
    @Override
    public String toString() {
        return "ID: " + vehicleID + ", Company: " + company;
    }
    
    public int getAvailableSeats(LocalDate date) throws SQLException {
        return dbHandler.getAvailableSeats(this.vehicleID, date);
    }

    // Get maximum capacity of the vehicle
    public int getMaxCapacity() throws SQLException {
        return dbHandler.getMaxVehicleCapacity(this.vehicleID);
    }

    // Update available seats for the vehicle on a given date
    public void updateAvailableSeats(LocalDate date, int change) throws SQLException {
        dbHandler.updateVehicleCapacity(this.vehicleID, date, change);
    }

    // Insert vehicle capacity on a specific date
    public void insertCapacity(LocalDate date, int availableSeats) throws SQLException {
        dbHandler.insertVehicleCapacity(this.vehicleID, date, availableSeats);
    }

    // Add the current user to the waitlist
    public static void addToWaitlist(int userID, int vehicleID, LocalDate date, int numTickets) throws SQLException {
        dbHandler.addToWaitlist(userID, vehicleID, date, numTickets);
    }

    // Get route distance for the vehicle
    public double getRouteDistance(int vehicleID) throws SQLException {
        return dbHandler.getRouteDistance(vehicleID);
    }

    // Get available vehicles for a route and type
    public static List<Vehicle> getAvailableVehicles(String startPoint, String endPoint, String type) throws SQLException {
        return dbHandler.getAvailableVehiclesByRouteAndType(startPoint, endPoint, type);
    }

}
