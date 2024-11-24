package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;

public class TravelHistoryController {

    @FXML
    private TextArea travelHistoryTA; // Changed to TextArea for multiline support

    @FXML
    private CheckBox fbdCb; // "Filter by Date" Checkbox

    @FXML
    private Button searchButton; // Search Button

    /**
     * Fetch and display travel history for the logged-in user.
     */
    @FXML
    public void showTravelHistory(ActionEvent event) {
        int userID = SessionManager.getInstance().getUserID(); // Get userID from session or login context

        // Base query to fetch bookings for the user
        String query = "SELECT * FROM booking WHERE userID = ?";

        // Add sorting condition if "Filter by Date" is selected
        if (fbdCb.isSelected()) {
            query += " ORDER BY bookingDate DESC";
        }

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userID);  // Set the userID in the query
            ResultSet rs = stmt.executeQuery();

            // Use StringBuilder to collect travel history lines
            StringBuilder travelHistory = new StringBuilder();

            while (rs.next()) {
                int bookingID = rs.getInt("bookingID");
                String bookingDate = rs.getString("bookingDate");
                String origin = rs.getString("origin");
                String destination = rs.getString("destination");
                String vehicleType = rs.getString("bookingType");
                int vehicleID = rs.getInt("vehicleID");

                // Format each booking as a single line
                travelHistory.append("Booking ID: ").append(bookingID)
                        .append(" | Date: ").append(bookingDate)
                        .append(" | Route: ").append(origin).append(" -> ").append(destination)
                        .append(" | Vehicle: ").append(vehicleType)
                        .append(" | VehicleID: ").append(vehicleID)
                        .append("\n"); // Separate each booking with a newline
            }

            // Display travel history in the TextArea
            if (travelHistory.length() > 0) {
                travelHistoryTA.setText(travelHistory.toString());
            } else {
                travelHistoryTA.setText("No travel history found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Error retrieving travel history: " + e.getMessage());
        }
    }

 
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
