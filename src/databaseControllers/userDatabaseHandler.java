package databaseControllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Classes.Complaint;
import Classes.User;
import javafx.scene.control.Alert;

public class userDatabaseHandler extends DatabaseHandler {
	
	
    public void resolveComplaints(int complaintID, boolean isSelected) {
    	String query = "UPDATE Complaint SET status = 'Resolved' WHERE complaintID = ? AND status = 'Submitted'";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, complaintID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert( "Success", "Complaint marked as resolved.");
                loadComplaints(isSelected);
            } else {
                showAlert( "No Changes", "No unresolved complaint found with the given ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update complaint status.");
        }
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
                            showAlert("Succesful!","Complaint submitted successfully!");
                        } else {
                            showAlert("Failiure","Failed to submit complaint.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.err.println("Error while inserting complaint: " + e.getMessage());
                    }
                } else {
                    showAlert("Invalid","No booking found for the user. Complaint cannot be submitted.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error while checking booking: " + e.getMessage());
        }
    }

    public String loadComplaints(boolean showAll) {
    	
    	Complaint complain = new Complaint();
        String query = showAll
                ? "SELECT * FROM Complaint"
                : "SELECT * FROM Complaint WHERE status = 'Submitted'";

        StringBuilder complaintsDisplay = new StringBuilder();

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int complaintID = rs.getInt("complaintID");
                int userID = rs.getInt("userID");
                int vehicleID = rs.getInt("vehicleID");
                String details = rs.getString("details");
                String status = rs.getString("status");

                complaintsDisplay.append("Complaint ID: ").append(complaintID).append("\n")
                        .append("User ID: ").append(userID).append("\n")
                        .append("Vehicle ID: ").append(vehicleID).append("\n")
                        .append("Details: ").append(details).append("\n")
                        .append("Status: ").append(status).append("\n\n");
            }

            return complaintsDisplay.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error loading complaints.";
        }
    }
    
    public User getUserByID(int userID) {
        String query = "SELECT * FROM User WHERE userID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("userID"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("cnic"),
                    rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE User SET name = ?, email = ?, cnic = ?, phone = ? WHERE userID = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getCnic());
            stmt.setString(4, user.getPhone());
            stmt.setInt(5, user.getUserID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
    
   
   public List<String> getTravelHistory(int userID, boolean filterByDate) {
       List<String> travelHistory = new ArrayList<>();

       // Base query to fetch bookings for the user
       String query = "SELECT * FROM booking WHERE userID = ?";

       // Add sorting condition if "Filter by Date" is selected
       if (filterByDate) {
           query += " ORDER BY bookingDate DESC";
       }

       try (Connection conn = DatabaseHandler.connect();
            PreparedStatement stmt = conn.prepareStatement(query)) {

           stmt.setInt(1, userID);  // Set the userID in the query
           ResultSet rs = stmt.executeQuery();

           while (rs.next()) {
               int bookingID = rs.getInt("bookingID");
               String bookingDate = rs.getString("bookingDate");
               String origin = rs.getString("origin");
               String destination = rs.getString("destination");
               String vehicleType = rs.getString("bookingType");
               int vehicleID = rs.getInt("vehicleID");

               // Format each booking as a single line
               String bookingDetails = "Booking ID: " + bookingID +
                       " | Date: " + bookingDate +
                       " | Route: " + origin + " -> " + destination +
                       " | Vehicle: " + vehicleType +
                       " | VehicleID: " + vehicleID;

               travelHistory.add(bookingDetails); // Add to the list
           }

       } catch (SQLException e) {
           e.printStackTrace();
           throw new RuntimeException("Error retrieving travel history: " + e.getMessage());
       }

       return travelHistory;
   }
   
   public boolean addToWaitlist(int userID, int vehicleID, LocalDate date, int ticketsRequested) {
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
	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
   
}
