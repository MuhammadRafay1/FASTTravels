package databaseControllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Classes.Vehicle;
import javafx.scene.control.Alert;

public class VehicleRouteDatabaseHandler extends DatabaseHandler{

    // Fetch the route distance by vehicleID
    public double getRouteDistance(int vehicleID) throws SQLException {
        String sql = "SELECT r.distance FROM route r " +
                     "JOIN vehicle v ON r.routeID = v.routeID " +
                     "WHERE v.vehicleID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("distance");
            }
        }
        throw new SQLException("No route found for the given vehicleID.");
    }
    
    public int getAvailableSeats(int vehicleID, LocalDate date) throws SQLException {
        String query = "SELECT availableSeats FROM VehicleCapacity WHERE vehicleID = ? AND date = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleID);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("availableSeats");
            }
        }
        return -1; // No entry found
    }

    public void insertVehicleCapacity(int vehicleID, LocalDate date, int availableSeats) throws SQLException {
        String query = "INSERT INTO VehicleCapacity (vehicleID, date, availableSeats) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleID);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setInt(3, availableSeats);
            stmt.executeUpdate();
        }
    }

    public void updateVehicleCapacity(int vehicleID, LocalDate date, int seatChange) throws SQLException {
        String query = "UPDATE VehicleCapacity SET availableSeats = availableSeats + ? WHERE vehicleID = ? AND date = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, seatChange);
            stmt.setInt(2, vehicleID);
            stmt.setDate(3, Date.valueOf(date));
            stmt.executeUpdate();
        }
    }

    public void addToWaitlist(int userID, int vehicleID, LocalDate date, int ticketsRequested) throws SQLException {
        String query = "INSERT INTO Waitlist (userID, vehicleID, date, ticketsRequested, waitlistPosition) "
                     + "SELECT ?, ?, ?, ?, COALESCE(MAX(waitlistPosition), 0) + 1 FROM Waitlist WHERE vehicleID = ? AND date = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, vehicleID);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setInt(4, ticketsRequested);
            stmt.setInt(5, vehicleID);
            stmt.setDate(6, Date.valueOf(date));
            stmt.executeUpdate();
        }
    }

    public List<Vehicle> getAvailableVehiclesByRouteAndType(String startPoint, String endPoint, String vehicleType) throws SQLException {
        String query = "SELECT v.vehicleID, v.company FROM route r " +
                       "JOIN vehicle v ON r.routeID = v.routeID " +
                       "WHERE r.startPoint = ? AND r.endPoint = ? AND v.type = ?";

        List<Vehicle> vehicles = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startPoint);
            stmt.setString(2, endPoint);
            stmt.setString(3, vehicleType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int vehicleID = rs.getInt("vehicleID");
                String company = rs.getString("company");
                vehicles.add(new Vehicle(vehicleID, company));
            }
        }
        return vehicles;
    }
    
    public boolean insertRoutes(String startPoint, String endPoint, double distance) {
        String query = "INSERT INTO Route (startPoint, endPoint, distance) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startPoint);
            stmt.setString(2, endPoint);
            stmt.setDouble(3, distance);

            // Check if the insertion was successful
            if (stmt.executeUpdate() > 0) {
                showAlert("Route added", "Successfully");
                return true;
            } else {
                showAlert("Route addition failed", "No rows affected.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean removeRoute(int routeID) {
    	 String query = "DELETE FROM Route WHERE routeID = ?";
         try (Connection conn = connect();
              PreparedStatement stmt = conn.prepareStatement(query)) {

             stmt.setInt(1, routeID);

             int rowsAffected = stmt.executeUpdate();
             if (rowsAffected > 0) {
                 showAlert("Route deleted successfully.","Deleted");
                 return true;
             } else {
                 showAlert("Error:", "No route found with ID " + routeID);
                 return false;
             }
         } catch (SQLException e) {
			e.printStackTrace();
		}
         return false;
    }
    
    public String getAllRoutes() {
        StringBuilder routes = new StringBuilder();
        // Modified query based on the Route table schema
        String query = "SELECT * FROM Route"; 
        Connection connection = connect();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int routeID = rs.getInt("routeID");  // routeID is an INT
                String startPoint = rs.getString("startPoint");
                String endPoint = rs.getString("endPoint");
                float distance = rs.getFloat("distance");

                // Format the route information
                routes.append("Route ID: ").append(routeID)
                      .append(" - Start: ").append(startPoint)
                      .append(" - End: ").append(endPoint)
                      .append(" - Distance: ").append(distance)
                      .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes.toString();
    }
    
    public boolean deleteVehicle(String vehicleID) {
        String query = "DELETE FROM vehicle WHERE vehicleID = ?";
        Connection connection = connect();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vehicleID);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean addVehicle(String companyName, String routeID, String seatingCapacity, String vehicleType) {
        String query = "INSERT INTO vehicle (company, routeID, capacity, type) VALUES (?, ?, ?, ?)";
        Connection connection = connect();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, companyName);
            stmt.setString(2, routeID);
            stmt.setInt(3, Integer.parseInt(seatingCapacity));
            stmt.setString(4, vehicleType);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getMaxVehicleCapacity(int vehicleID) throws SQLException {
        String query = "SELECT capacity FROM Vehicle WHERE vehicleID = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("capacity");
            } else {
                throw new SQLException("Vehicle not found with ID: " + vehicleID);
            }
        }
    }

}
