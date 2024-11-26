package Classes;

public class Route {
    private int routeID;
    private String startPoint;
    private String endPoint;
    private double distance;
    private String vehicleType; // Added to accommodate vehicle type

    // Default constructor
    public Route() {}

    // Constructor for routes without vehicle type
    public Route(int routeID, String startPoint, String endPoint, float distance) {
        this.routeID = routeID;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distance = distance;
    }

    // Constructor for routes with vehicle type
    public Route(int routeID, String startPoint, String endPoint, float distance, String vehicleType) {
        this.routeID = routeID;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distance = distance;
        this.vehicleType = vehicleType;
    }

    public Route(String source, String destination) {
    	this.startPoint = source;
    	this.endPoint = destination;
		// TODO Auto-generated constructor stub
	}

	// Getter and Setter for routeID
    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    // Getter and Setter for startPoint
    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    // Getter and Setter for endPoint
    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    // Getter and Setter for distance
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    // Getter and Setter for vehicleType
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    // toString method to display route details
    @Override
    public String toString() {
        String routeDetails = "Route: " + startPoint + " to " + endPoint + " (" + distance + " km)";
        if (vehicleType != null) {
            routeDetails += " | Vehicle Type: " + vehicleType;
        }
        return routeDetails;
    }
}
