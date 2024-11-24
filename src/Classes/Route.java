package Classes;
public class Route {
    private int routeID;
    private String startPoint;
    private String endPoint;
    private float distance;

    public Route(int routeID, String startPoint, String endPoint, float distance) {
        this.routeID = routeID;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distance = distance;
    }

    public int getRouteID() {
        return routeID;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Route: " + startPoint + " to " + endPoint + " (" + distance + " km)";
    }
}
