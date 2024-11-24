package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.io.IOException;
import java.sql.*;

import Classes.Complaint;
public class adminManageComplaintsController {
	
	    @FXML
	    private Button backToDashboardButton;

	    @FXML
	    private TextField complaintIDToResolveTB;

	    @FXML
	    private TextField complaintsTextArea;

	    @FXML
	    private Button markAsResolvedButton;

	    @FXML
	    private CheckBox showAllCB;
	    
	    DatabaseHandler dbHandler = new DatabaseHandler();
	    
	    @FXML
	    public void initialize() {
	        loadComplaints(false); // By default, show only "Submitted" complaints
	        showAllCB.setOnAction(event -> loadComplaints(showAllCB.isSelected()));
	    }

	    /**
	     * Fetches complaints from the database and displays them in the complaintsTextArea.
	     * 
	     * @param showAll If true, displays all complaints. Otherwise, only displays "Submitted" complaints.
	     */
	    private void loadComplaints(boolean showAll) {
	    	
	    	Complaint complain = new Complaint();
	        String query = showAll
	                ? "SELECT * FROM Complaint"
	                : "SELECT * FROM Complaint WHERE status = 'Submitted'";

	        StringBuilder complaintsDisplay = new StringBuilder();

	        try (Connection conn = dbHandler.connect();
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

	            complaintsTextArea.setText(complaintsDisplay.toString());

	        } catch (SQLException e) {
	            e.printStackTrace();
	            complaintsTextArea.setText("Error loading complaints.");
	        }
	    }

	    
	    @FXML
	    public void markComplaintAsResolved(ActionEvent event) throws IOException {
	        String complaintIDText = complaintIDToResolveTB.getText();

	        if (complaintIDText.isEmpty()) {
	            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a Complaint ID.");
	            return;
	        }

	        int complaintID;
	        try {
	            complaintID = Integer.parseInt(complaintIDText);
	        } catch (NumberFormatException e) {
	            showAlert(Alert.AlertType.ERROR, "Input Error", "Complaint ID must be a valid number.");
	            return;
	        }

	        String query = "UPDATE Complaint SET status = 'Resolved' WHERE complaintID = ? AND status = 'Submitted'";

	        try (Connection conn = dbHandler.connect();
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            stmt.setInt(1, complaintID);

	            int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected > 0) {
	                showAlert(Alert.AlertType.INFORMATION, "Success", "Complaint marked as resolved.");
	                loadComplaints(showAllCB.isSelected());
	            } else {
	                showAlert(Alert.AlertType.WARNING, "No Changes", "No unresolved complaint found with the given ID.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update complaint status.");
	        }
	    }

	    @FXML
	    public void goToMainDashboard(ActionEvent event) throws IOException {
	        try {
	            // Load the RegisterPage.fxml
	            AnchorPane root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
	            
	            // Get the current stage and set the new scene
	            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            System.err.println("Error loading AdminDashboard.fxml: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    
	    private void showAlert(Alert.AlertType alertType, String title, String message) {
	        Alert alert = new Alert(alertType);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }

}
