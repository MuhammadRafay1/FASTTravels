package databaseControllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import application.SessionManager;
import javafx.scene.control.Alert;
import Classes.RouteDetails;
public class BookingDatabaseHandler extends DatabaseHandler{
    
    public float loadBookingDetailsWithBookingID(int bookingID) {
    	
    	String query = "SELECT fare FROM Booking WHERE bookingID = ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingID); // Set the Booking ID
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                 // Set the fare amount
                System.out.println("Fare: " + rs.getFloat("fare"));
                return rs.getFloat("fare");
            } else {
                System.out.println("No booking found for bookingID: " + bookingID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load booking details: " + e.getMessage());
        }
		return 0.0f;	
    }
    
    
    public void insertingToPayment(int bookingID,float amount) {
    	String query = """
                INSERT INTO Payment (bookingID, amount, status, paymentType)
                VALUES (?, ?, 'Unpaid', 'Cash')
                """;

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingID); // Set the Booking ID
            stmt.setFloat(2, amount); // Set the Payment Amount

            stmt.executeUpdate(); // Execute the query
            showAlert("Payment Method: Cash", "Pay on arrival. Your payment is marked as pending.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to process payment: " + e.getMessage());
        }
    }

    public int insertBooking(int vehicleId, LocalDate date, int numTickets, String source, String destination, String bookingType, int userId, double fare) {
        String query = "INSERT INTO Booking (userID, bookingType, bookingDate, origin, destination, fare, numTickets, vehicleID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int bookingID = -1;
        try (Connection conn = connect(); // Assuming connect() gets a DB connection
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters for the query
            stmt.setInt(1, userId);                         // userID
            stmt.setString(2, bookingType);                 // Booking type ('Bus', 'Train', 'Flight')
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            stmt.setString(3, formattedDate);               // Booking date
            stmt.setString(4, source);                      // Origin (source)
            stmt.setString(5, destination);                 // Destination
            stmt.setDouble(6, fare);                        // Fare
            stmt.setInt(7, numTickets);                     // Number of tickets
            stmt.setInt(8, vehicleId);                      // Vehicle ID

            // Execute the query
            stmt.executeUpdate();

            // Retrieve the generated bookingID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bookingID = generatedKeys.getInt(1); // Retrieve the generated bookingID
                    System.out.println("Booking inserted successfully with bookingID: " + bookingID);

                    // Save the bookingID globally if needed (e.g., in SessionManager)
                    SessionManager.getInstance().setBookingID(bookingID);
                } else {
                    System.out.println("Failed to retrieve bookingID.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting booking: " + e.getMessage());
            return 0;
        }
		return bookingID;
    }
    
    public boolean insertCargoBooking(int bookingID, int userID, int vehicleID, int weight, int height, int width, LocalDate date) {
        String query = "INSERT INTO CargoBooking (cargoBookingID, userID, routeID, vehicleID, weight, widht, height, bdate) " +
                       "VALUES (?, ?, (SELECT routeID FROM vehicle WHERE vehicleID = ?), ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set parameters for the query
            stmt.setInt(1, bookingID);              // cargoBookingID (same as main booking ID)
            stmt.setInt(2, userID);                 // userID
            stmt.setInt(3, vehicleID);              // vehicleID (used to fetch routeID)
            stmt.setInt(4, vehicleID);              // vehicleID for the cargo booking
            stmt.setInt(5, weight);                 // Weight of the cargo
            stmt.setInt(6, width);                  // Width of the cargo
            stmt.setInt(7, height);                 // Height of the cargo
            stmt.setDate(8, Date.valueOf(date));    // Booking date

            // Execute the query
            stmt.executeUpdate();
            System.out.println("Cargo booking inserted successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting cargo booking: " + e.getMessage());
        }
        return false;
    }
    
    public RouteDetails getRouteDetailsByVehicleID(int vehicleID) {
        String query = "SELECT r.distance, v.type FROM route r " +
                       "JOIN vehicle v ON r.routeID = v.routeID " +
                       "WHERE v.vehicleID = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, vehicleID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double distance = rs.getDouble("distance");
                String type = rs.getString("type");
                return new RouteDetails(distance, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching route details: " + e.getMessage());
        }
        return null;
    }

    
    /////////////////////////////////////////////////////////////////////////
    public Map<Integer, String> getAllTravelBookingsForUser(int userID) {
        Map<Integer, String> bookings = new HashMap<>();
        String query = "SELECT bookingID, CONCAT(origin, ' to ', destination) AS route FROM Booking WHERE userID = ? AND bookingType NOT IN ('Cargo')";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.put(rs.getInt("bookingID"), rs.getString("route"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Map<Integer, String> getAllCargoBookingsForUser(int userID) {
        Map<Integer, String> bookings = new HashMap<>();
        String query = "SELECT bookingID, CONCAT(origin, ' to ', destination) AS route FROM Booking WHERE userID = ? AND bookingType = 'Cargo'";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.put(rs.getInt("bookingID"), rs.getString("route"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Map<String, String> getTravelBookingDetails(int bookingID) {
        Map<String, String> details = new HashMap<>();
        String query = "SELECT origin, destination, bookingDate FROM Booking WHERE bookingID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                details.put("origin", rs.getString("origin"));
                details.put("destination", rs.getString("destination"));
                details.put("bookingDate", rs.getDate("bookingDate").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public Map<String, String> getCargoBookingDetails(int cargoBookingID) {
        Map<String, String> details = new HashMap<>();
        String query = "SELECT origin, destination, bookingDate FROM booking WHERE bookingID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, cargoBookingID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                details.put("origin", rs.getString("origin"));
                details.put("destination", rs.getString("destination"));
                details.put("bookingDate", rs.getDate("bookingDate").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }

    public void updateTravelBookingDate(int bookingID, LocalDate newDate) {
        String query = "UPDATE Booking SET bookingDate = ? WHERE bookingID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(newDate));
            stmt.setInt(2, bookingID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCargoBookingDate(int cargoBookingID, LocalDate newDate) {
        String query = "UPDATE CargoBooking cb INNER JOIN Booking b ON cb.cargoBookingID = b.bookingID SET cb.bookingDate = ?, b.bookingDate = ? WHERE cb.cargoBookingID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(newDate));
            stmt.setDate(2, Date.valueOf(newDate));
            stmt.setInt(3, cargoBookingID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTravelBooking(int bookingID) {
        String query = "DELETE FROM Booking WHERE bookingID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCargoBooking(int cargoBookingID) {
        String query = "DELETE FROM CargoBooking WHERE cargoBookingID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, cargoBookingID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Deleting from Booking table if cascade is not set in DB
        String query2 = "DELETE FROM Booking WHERE bookingID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt2 = conn.prepareStatement(query2)) {
            stmt2.setInt(1, cargoBookingID);
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int checkAvailability(int vehicleID, LocalDate date) {
        String query = "SELECT availableSeats FROM VehicleCapacity WHERE vehicleID = ? AND date = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleID);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("availableSeats");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Indicates no data found or error
    }
    
    public boolean updateCapacity(int vehicleID, LocalDate date, int ticketsBooked) {
        String query = "UPDATE VehicleCapacity SET availableSeats = availableSeats - ? WHERE vehicleID = ? AND date = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticketsBooked);
            stmt.setInt(2, vehicleID);
            stmt.setDate(3, Date.valueOf(date));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
