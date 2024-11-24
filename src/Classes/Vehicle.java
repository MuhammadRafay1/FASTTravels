package Classes;


public class Vehicle {
    private int vehicleID;
    private String type;
    private int capacity;
    private String availabilityStatus;
    private int routeID;
    private String company;

    public Vehicle(int vehicleID, String type, int capacity, String availabilityStatus, String company) {
        this.vehicleID = vehicleID;
        this.type = type;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
        this.company = company;
    }

    public Vehicle(int vehicleID1, String company2) {
		this.vehicleID = vehicleID1;
		this.company = company2;
	}

	public int getVehicleID() {
        return vehicleID;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public String getCompany() {
        return company;
    }
    
    public double calculateFare(double distance, int numTickets, String type) {
        double fare = 0.0;

        if (type.equalsIgnoreCase("Flight")) {
            fare = (distance * 10) * numTickets;
        } else if (type.equalsIgnoreCase("Bus")) {
            fare = (distance * 4) * numTickets;
        } else if (type.equalsIgnoreCase("Train")) {
            fare = (distance * 2) * numTickets;
        }

        return fare;
    }
    
    @Override
    public String toString() {
        return "ID: " + vehicleID + ", Company: " + company;
    }
}

