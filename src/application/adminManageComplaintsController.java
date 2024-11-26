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
import databaseControllers.userDatabaseHandler;
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
	    
	    userDatabaseHandler dbUserHandler = new userDatabaseHandler();
	   // adminDatabaseController adminDbHanlder = new adminDatabaseController();
	   
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
	    	
	    	String text;
	    	text = dbUserHandler.loadComplaints(showAll); 
	    	complaintsTextArea.setText(text);
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

	        dbUserHandler.resolveComplaints(complaintID,showAllCB.isSelected());
	    }

	    @FXML
	    public void goToMainDashboard(ActionEvent event) throws IOException {
	        try {
	            // Load the RegisterPage.fxml
	            AnchorPane root = FXMLLoader.load(getClass().getResource("/fxmlFiles/AdminDashboard.fxml"));
	            
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
