package databaseControllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;

public class paymentDatabaseController extends DatabaseHandler {
	    
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

}
