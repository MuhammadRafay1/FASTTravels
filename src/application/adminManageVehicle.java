package application;

import java.io.IOException;

import Classes.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import databaseControllers.*;

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
    private userDatabaseHandler dbUserHandler = new userDatabaseHandler();
    private Admin admin = dbUserHandler.getAdminById(1);

    // Load available routes in the text area
    @FXML
    public void initialize() {
        // Fetch and display available routes when the page loads
        String allRoutes = admin.getAllRoutes();
        allRoutesTA.setText(allRoutes);
    }

    // Save a new vehicle
    private void loadRoutes() {
        String allRoutes = admin.getAllRoutes();
        allRoutesTA.setText(allRoutes);
    }

    // Save a new vehicle
    @FXML
    public void saveVehicle(ActionEvent event) {
        String companyName = companyNameTB.getText();
        String routeID = routeIDforVehicleTB.getText();
        String seatingCapacity = seatingCapacityTB.getText();
        String vehicleType = vehicletypeTB.getText();

        if (companyName.isEmpty() || routeID.isEmpty() || seatingCapacity.isEmpty() || vehicleType.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        boolean success = admin.addVehicle(companyName, routeID, seatingCapacity, vehicleType);

        if (success) {
            showAlert("Success", "Vehicle added successfully.");
        } else {
            showAlert("Error", "Failed to add the vehicle. Please try again.");
        }
    }

    // Delete a vehicle
    @FXML
    public void deleteVehicle(ActionEvent event) {
        String vehicleID = vehicleIDtoDeleteTB.getText();

        if (vehicleID.isEmpty()) {
            showAlert("Error", "Vehicle ID is required.");
            return;
        }

        boolean success = admin.deleteVehicle(vehicleID);

        if (success) {
            showAlert("Success", "Vehicle deleted successfully.");
        } else {
            showAlert("Error", "Failed to delete the vehicle. Please try again.");
        }
    }

    // Navigate back to the admin menu
    @FXML
    public void backToMenu(ActionEvent event) {
        try {
            // Load the Admin Dashboard
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
