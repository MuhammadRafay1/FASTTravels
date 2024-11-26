package Classes;

public class RouteDetails {
    private double distance;
    private String type;

    public RouteDetails(double distance, String type) {
        this.distance = distance;
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public String getType() {
        return type;
    }
}
