package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Classes.*;
import javafx.scene.control.Alert;

public class DatabaseHandler {
    private final static String url = "jdbc:mysql://localhost:3306/fasttravelsdatabase";
    private final static String user = "root";
    private final static String password = "123456";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
            return null;
        }
    }
    
    
    public Vehicle getVehicleByID(int vehicleID) throws SQLException {
        String sql = "SELECT vehicleID, type, capacity, availabilityStatus, company, routeID " +
                     "FROM vehicle WHERE vehicleID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                    rs.getInt("vehicleID"),
                    rs.getString("type"),
                    rs.getInt("capacity"),
                    rs.getString("availabilityStatus"),
                    rs.getString("company")
                );
            }
        }
        return null; // Return null if no vehicle is found
    }

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

    // Insert a new user into the database (either admin or customer based on isAdmin)
    public boolean registerUser(User user) {
        String userQuery = "INSERT INTO user (name, email, password, cnic, phone) VALUES (?, ?, ?, ?, ?)";
        String customerUserQuery = "INSERT INTO customeruser (userID, loyaltypoints) VALUES (?, ?)";

        try (Connection conn = connect(); 
             PreparedStatement userStmt = conn.prepareStatement(userQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement customerUserStmt = conn.prepareStatement(customerUserQuery)) {

            // Insert into user table
            userStmt.setString(1, user.getName());
            userStmt.setString(2, user.getEmail());
            userStmt.setString(3, user.getPassword());  // Note: Storing plain text passwords is not secure
            userStmt.setString(4, user.getCnic());
            userStmt.setString(5, user.getPhone());
            int rowsInserted = userStmt.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve generated userID
                ResultSet generatedKeys = userStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userID = generatedKeys.getInt(1);

                    // Insert into customeruser table
                    customerUserStmt.setInt(1, userID);
                    customerUserStmt.setInt(2, 50);  // Initial loyalty points
                    int customerRowsInserted = customerUserStmt.executeUpdate();

                    return customerRowsInserted > 0;  // Returns true if both insertions were successful
                }
            }
            return false; // Return false if user insertion failed or no key was generated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//
    // Check if the username and password match an existing user
    public int[] validateLogin(String username, String password) {
        String query = "SELECT userID, userType FROM User WHERE name = ? AND password = ?";
        int[] values = new int[2];  // Array to store userID and role
        
        try (Connection conn = connect(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters for the query
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                values[0] = rs.getInt("userID");  // Store userID
                String role = rs.getString("userType");

                // Assign role (0 = customer, 1 = admin)
                if ("Admin".equals(role)) {
                    values[1] = 1;
                } else {
                    values[1] = 0;
                }
            } else {
                values[0] = -1;  // No user found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error validating login: " + e.getMessage());
        }

        return values;  // Return userID and role
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

    public static boolean saveTrainBooking(TrainBooking booking) {
		return false;
        // Similar logic to saveTrainBooking with specific train details
    }
    
    public void insertCargoBooking(int bookingID, int userID, int vehicleID, int weight, int height, int width, LocalDate date) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting cargo booking: " + e.getMessage());
        }
    }

    
    public void insertComplaint(int userID, int vehicleID, String details) {
        String checkBookingQuery = "SELECT COUNT(*) FROM Booking WHERE userID = ?";
        String insertComplaintQuery = "INSERT INTO Complaint (userID, vehicleID, details) VALUES (?, ?, ?)";
        
        try (Connection conn = connect();  
             PreparedStatement checkStmt = conn.prepareStatement(checkBookingQuery)) {

            // Check if the user has made any booking
            checkStmt.setInt(1, userID);  // Set the userID
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int bookingCount = rs.getInt(1);  // Get the count of bookings for the user
                
                if (bookingCount > 0) {
                    // User has a booking, proceed with inserting the complaint
                    
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertComplaintQuery)) {
                        // Set the parameters for the insert statement
                        insertStmt.setInt(1, userID);  // Set the userID
                        insertStmt.setInt(2, vehicleID);  // Set the vehicleID
                        insertStmt.setString(3, details);  // Set the complaint details

                        // Execute the update
                        int rowsAffected = insertStmt.executeUpdate();
                        
                        if (rowsAffected > 0) {
                            System.out.println("Complaint submitted successfully!");
                        } else {
                            System.out.println("Failed to submit complaint.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error while inserting complaint: " + e.getMessage());
                    }
                } else {
                    System.out.println("No booking found for the user. Complaint cannot be submitted.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while checking booking: " + e.getMessage());
        }
    }
    
    private int fetchUserIDFromDatabase(String username) {
        String query = "SELECT userID FROM User WHERE name = ?"; // Adjust column names as needed
        try (Connection conn = DatabaseHandler.connect(); // Establish DB connection
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username); // Bind the username parameter
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("userID"); // Retrieve the userID column
            } else {
                System.out.println("User not found with username: " + username);
                return 0; // Return 0 or handle user-not-found appropriately
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching userID from database: " + e.getMessage());
            return 0; // Return 0 or a negative value to indicate failure
        }
    }


    public static boolean saveFlightBooking(FlightBooking booking) {
		return false;
        // Similar logic to saveFlightBooking with specific flight details
    }
    
    public void insertIntoCardPayment(String cardHolderName,String cardNumber,String cvv,int bookingID,float amount) {
    	try (Connection conn = DatabaseHandler.connect()) {
            // Insert into Payment table
            String insertPaymentQuery = "INSERT INTO Payment (bookingID, amount, status, paymentType) VALUES (?, ?, 'Paid', 'Card')";
            PreparedStatement paymentStmt = conn.prepareStatement(insertPaymentQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            paymentStmt.setInt(1, bookingID);
            paymentStmt.setFloat(2, amount);
            paymentStmt.executeUpdate();

            // Retrieve the generated paymentID
            ResultSet rs = paymentStmt.getGeneratedKeys();
            int paymentID = 0;
            if (rs.next()) {
                paymentID = rs.getInt(1);
            } else {
                showAlert("Error", "Failed to retrieve Payment ID.");
                return;
            }

            // Insert into CardPayment table
            String insertCardPaymentQuery = "INSERT INTO CardPayment (paymentID, cardNumber, cvv,  cardHolderName) VALUES (?, ?, ?, ?)";
            PreparedStatement cardPaymentStmt = conn.prepareStatement(insertCardPaymentQuery);
            cardPaymentStmt.setInt(1, paymentID);
            cardPaymentStmt.setString(2, cardNumber);
            cardPaymentStmt.setString(3, cvv);
            cardPaymentStmt.setString(4, cardHolderName);
            cardPaymentStmt.executeUpdate();

            showAlert("Success", "Payment successfully processed!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to process payment: " + e.getMessage());
        }
    }
    
    public float fetchAmountForBooking(int bookingID) {
        String query = "SELECT fare FROM Booking WHERE bookingID = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("fare");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch amount for booking: " + e.getMessage());
        }
        return 0.0f; // Default amount if no record is found
    }
    
    public int getLoyaltyPoints(int userID) {
    	 String query = "SELECT loyaltyPoints FROM customeruser WHERE userID = ?";
         try (Connection conn = DatabaseHandler.connect();
              PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setInt(1, userID);
             ResultSet rs = stmt.executeQuery();
             if (rs.next()) {
                 return rs.getInt("loyaltyPoints");
             }
         } catch (SQLException e) {
             e.printStackTrace();
             showAlert("Error", "Failed to fetch loyaltyPoints:" + e.getMessage());
         }
		return 0;
    	
    }
    
    public void redeemLoyaltyPoints(int userID) {
        String getPointsQuery = "SELECT loyaltyPoints FROM customeruser WHERE userID = ?";
        String updatePointsQuery = "UPDATE customeruser SET loyaltyPoints = 0 WHERE userID = ?";
        String checkWalletQuery = "SELECT COUNT(*) AS count FROM UserWallet WHERE userID = ?";
        String insertWalletQuery = "INSERT INTO UserWallet (userID, walletAmount) VALUES (?, ?)";
        String updateWalletQuery = "UPDATE UserWallet SET walletAmount = walletAmount + ? WHERE userID = ?";

        try (Connection conn = DatabaseHandler.connect()) {
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Get the current loyalty points
            int loyaltyPoints = 0;
            try (PreparedStatement getPointsStmt = conn.prepareStatement(getPointsQuery)) {
                getPointsStmt.setInt(1, userID);
                ResultSet rs = getPointsStmt.executeQuery();
                if (rs.next()) {
                    loyaltyPoints = rs.getInt("loyaltyPoints");
                }
            }

            if (loyaltyPoints > 0) {
                // Step 2: Update loyalty points to 0
                try (PreparedStatement updatePointsStmt = conn.prepareStatement(updatePointsQuery)) {
                    updatePointsStmt.setInt(1, userID);
                    updatePointsStmt.executeUpdate();
                }

                // Step 3: Check if a wallet entry exists for the user
                boolean walletExists = false;
                try (PreparedStatement checkWalletStmt = conn.prepareStatement(checkWalletQuery)) {
                    checkWalletStmt.setInt(1, userID);
                    ResultSet rs = checkWalletStmt.executeQuery();
                    if (rs.next()) {
                        walletExists = rs.getInt("count") > 0;
                    }
                }

                // Step 4: Insert or update the wallet entry
                if (walletExists) {
                    try (PreparedStatement updateWalletStmt = conn.prepareStatement(updateWalletQuery)) {
                        updateWalletStmt.setInt(1, loyaltyPoints);
                        updateWalletStmt.setInt(2, userID);
                        updateWalletStmt.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertWalletStmt = conn.prepareStatement(insertWalletQuery)) {
                        insertWalletStmt.setInt(1, userID);
                        insertWalletStmt.setInt(2, loyaltyPoints);
                        insertWalletStmt.executeUpdate();
                    }
                }

                conn.commit(); // Commit the transaction
                showAlert("Loyalty points redeemed successfully.", "Wallet updated.");
            } else {
                showAlert("No loyalty points to redeem.","Earn Loyalty points by booking rides!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during loyalty points redemption: " + e.getMessage());
        }
    }
    
    public void insertRoutes(String startPoint, String endPoint, double distance) {
        String query = "INSERT INTO Route (startPoint, endPoint, distance) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, startPoint);
            stmt.setString(2, endPoint);
            stmt.setDouble(3, distance);

            // Check if the insertion was successful
            if (stmt.executeUpdate() > 0) {
                showAlert("Route added", "Successfully");
            } else {
                showAlert("Route addition failed", "No rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void removeRoute(int routeID) {
    	 String query = "DELETE FROM Route WHERE routeID = ?";
         try (Connection conn = connect();
              PreparedStatement stmt = conn.prepareStatement(query)) {

             stmt.setInt(1, routeID);

             int rowsAffected = stmt.executeUpdate();
             if (rowsAffected > 0) {
                 showAlert("Route deleted successfully.","Deleted");
             } else {
                 showAlert("Error:", "No route found with ID " + routeID);
             }
         } catch (SQLException e) {
			e.printStackTrace();
		}
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

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        String query = "INSERT INTO vehicle (companyname, routeID, capacity, type) VALUES (?, ?, ?, ?)";
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
    
    //////////////////////////////////////////////////////////////////////////////////
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
    
}

