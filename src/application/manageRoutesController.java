package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import Classes.Admin;
import databaseControllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class manageRoutesController {
	
	    @FXML
	    private TextArea allRoutesTextArea;

	    @FXML
	    private Button backToMEnuButton;

	    @FXML
	    private Button deleteRouteButton;

	    @FXML
	    private TextField routeDistanceTB;

	    @FXML
	    private TextField routeEndPointTB;

	    @FXML
	    private TextField routeIDtoDeleteTB;

	    @FXML
	    private TextField routeStartPointTB;

	    @FXML
	    private Button saveRouteButton;
	
	    userDatabaseHandler dbUserHandler = new userDatabaseHandler();
	    private Admin admin = dbUserHandler.getAdminById(1);
	    
	    public void initialize() {
	        populateAllRoutes(); // Populate the text area when the scene loads
	    }
	   
	    @FXML
	    public void saveRoute(ActionEvent event) {
	        String startPoint = routeStartPointTB.getText();
	        String endPoint = routeEndPointTB.getText();
	        String distanceText = routeDistanceTB.getText();

	        if (startPoint.isEmpty() || endPoint.isEmpty() || distanceText.isEmpty()) {
	            showAlert("Error", "All fields must be filled to save a route!");
	            return;
	        }

	        try {
	            double distance = Double.parseDouble(distanceText);
	            boolean success = admin.addRoute(startPoint, endPoint, distance);

	            if (success) {
	                showAlert("Success", "Route saved successfully!");
	                populateAllRoutes(); // Refresh the routes display
	            } else {
	                showAlert("Error", "Failed to save the route. Please try again.");
	            }
	        } catch (NumberFormatException e) {
	            showAlert("Error", "Distance must be a valid number!");
	        }
	    }
	    
	    @FXML
	    public void populateAllRoutes() {
	        String allRoutes = admin.getAllRoutes();
	        allRoutesTextArea.setText(allRoutes);
	    }

	    // Delete a route
	    @FXML
	    public void deleteRoute(ActionEvent event) {
	        String routeIdText = routeIDtoDeleteTB.getText();

	        if (routeIdText.isEmpty()) {
	            showAlert("Error", "Route ID must be entered to delete a route.");
	            return;
	        }

	        try {
	            int routeID = Integer.parseInt(routeIdText);
	            boolean success = admin.deleteRoute(routeID);

	            if (success) {
	                showAlert("Success", "Route deleted successfully!");
	                populateAllRoutes(); // Refresh the routes display
	            } else {
	                showAlert("Error", "Failed to delete the route. Please try again.");
	            }
	        } catch (NumberFormatException e) {
	            showAlert("Error", "Route ID must be a valid number!");
	        }
	    }

	    // Navigate back to the main dashboard
	    @FXML
	    public void goToMainDashboard(ActionEvent event) {
	        try {
	            // Load the AdminDashboard.fxml
	            AnchorPane root = FXMLLoader.load(getClass().getResource("/fxmlFiles/AdminDashboard.fxml"));
	            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            System.err.println("Error loading AdminDashboard.fxml: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    // Utility method to show alerts
	    private void showAlert(String title, String message) {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
}
