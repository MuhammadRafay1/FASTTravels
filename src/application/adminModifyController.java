package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import application.LoginController;
public class adminModifyController {
	
	    @FXML
	    private TextField cnicTb;

	    @FXML
	    private Button commitChangesButton;

	    @FXML
	    private TextField emailTb;

	    @FXML
	    private TextField pNumTb;

	    @FXML
	    private TextField userIDTb;

	    @FXML
	    private TextField usernameTB;
	    
	    @FXML
	    private Button viewSavedInfoButton;
	    
	    private int currentUserID; // Store the user ID for the currently displayed user

	    // Method to populate the form with user data
	    public void loadUserData(ActionEvent event) throws IOException {
	        this.currentUserID = LoginController.userID;

	        // SQL Query to fetch user data by ID
	        String query = "SELECT * FROM User WHERE userID = ?";
	        try (Connection conn = DatabaseHandler.connect();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	        	currentUserID = SessionManager.getInstance().getUserID();
	        	System.out.println("Logged-in User ID: " + currentUserID);
	            stmt.setInt(1,currentUserID);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                System.out.println("Name: " + rs.getString("name"));
	                System.out.println("Email: " + rs.getString("email"));
	            	userIDTb.setText(String.valueOf(rs.getInt("userID")));
	            	usernameTB.setText(rs.getString("name"));
	            	emailTb.setText(rs.getString("email"));
	            	cnicTb.setText(rs.getString("cnic"));
	            	pNumTb.setText(rs.getString("phone"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Error", "Failed to load user data: " + e.getMessage());
	        }
	    }
	    
	    @FXML
	    public void onSave(ActionEvent event) {
	        String name = usernameTB.getText();
	        String email = emailTb.getText();
	        String cnic = cnicTb.getText();
	        String phone = pNumTb.getText();

	        // SQL Query to update user data
	        String query = "UPDATE User SET name = ?, email = ?,  cnic = ?, phone = ? WHERE userID = ?";
	        try (Connection conn = DatabaseHandler.connect();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setString(1, name);
	            stmt.setString(2, email);
	            stmt.setString(3, cnic);
	            stmt.setString(4, phone);
	            stmt.setInt(5, currentUserID);

	            int rowsUpdated = stmt.executeUpdate();
	            if (rowsUpdated > 0) {
	                showAlert("Success", "User data updated successfully!");
	            } else {
	                showAlert("Error", "Failed to update user data.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Error", "Failed to update user data: " + e.getMessage());
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

	    // Utility method to show an alert
	    private void showAlert(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }

}
