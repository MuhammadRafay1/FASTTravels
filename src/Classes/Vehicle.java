package Classes;

public class Vehicle {
    private int vehicleID;
    private String type;
    private int capacity;
    private String availabilityStatus;
    private int routeID;
    private String company;

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
}
