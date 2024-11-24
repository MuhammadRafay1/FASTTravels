package application;

import java.io.IOException;

import application.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class adminManageVehicle {
	
	@FXML
    private TextArea allRoutesTA;

    @FXML
    private TextArea allVehiclesTextArea;

    @FXML
    private TextField availableStatus;

    @FXML
    private Button backToMEnuButton;

    @FXML
    private TextField companyNameTB;

    @FXML
    private Button deleteVehicleButton;

    @FXML
    private TextField routeIDforVehicleTB;

    @FXML
    private Button saveRouteButton;

    @FXML
    private TextField seatingCapacityTB;

    @FXML
    private TextField vehicleIDtoDeleteTB;

    @FXML
    private TextField vehicletypeTB;
    
    private DatabaseHandler dbController = new DatabaseHandler();

    // Load available routes in the text area
    @FXML
    public void initialize() {
        // Fetch and display available routes when the page loads
        String allRoutes = dbController.getAllRoutes();
        allRoutesTA.setText(allRoutes);
    }

    // Save a new vehicle
    @FXML
    public void saveVehicle(ActionEvent event) throws IOException {
        String companyName = companyNameTB.getText();
        String routeID = routeIDforVehicleTB.getText();
        String seatingCapacity = seatingCapacityTB.getText();
        String vehicleType = vehicletypeTB.getText();

        if (companyName.isEmpty() || routeID.isEmpty() || seatingCapacity.isEmpty() || vehicleType.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        boolean success = dbController.addVehicle(companyName, routeID, seatingCapacity, vehicleType);

        if (success) {
            showAlert("Success", "Vehicle added successfully.");
        } else {
            showAlert("Error", "Failed to add the vehicle. Please try again.");
        }
    }

    // Delete a vehicle
    @FXML
    public void deleteVehicle(ActionEvent event) throws IOException {
        String vehicleID = vehicleIDtoDeleteTB.getText();

        if (vehicleID.isEmpty()) {
            showAlert("Error", "Vehicle ID is required.");
            return;
        }

        boolean success = dbController.deleteVehicle(vehicleID);

        if (success) {
            showAlert("Success", "Vehicle deleted successfully.");
        } else {
            showAlert("Error", "Failed to delete the vehicle. Please try again.");
        }
    }
    

    // Navigate back to the menu
    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
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
        }  // Code to navigate back to the admin dashboard or menu
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
