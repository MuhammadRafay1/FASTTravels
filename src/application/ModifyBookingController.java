package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import databaseControllers.*
;public class ModifyBookingController {

    @FXML
    private ChoiceBox<Integer> allCargoTravelBookingCB;

    @FXML
    private ChoiceBox<Integer> allTravelBookingCB;

    @FXML
    private Button cancelBookingButton;

    @FXML
    private Button cancelCargoBookingButton;

    @FXML
    private TextField cargoBookingTB;

    @FXML
    private DatePicker cargoDatePicker;

    @FXML
    private TextField cargoDateTB;

    @FXML
    private TextField cargoDestinationTb;

    @FXML
    private TextField cargoSourceTB;

    @FXML
    private Button commitCargoChangesButton;

    @FXML
    private Button commitChangesButton;

    @FXML
    private TextField dateTB;

    @FXML
    private TextField destinationTb;

    @FXML
    private Button goToMainDashboard;

    @FXML
    private Label headingLabel;

    @FXML
    private TextField sourceTB;

    @FXML
    private TextField travelBookingtb;

    @FXML
    private DatePicker travelDatePicker;
    
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final BookingDatabaseHandler dbBookingHandler = new BookingDatabaseHandler();
     int userID = SessionManager.getInstance().getUserID();
    // Load all bookings for the user into the choice boxes
     @FXML
     public void initialize() {
    	 
    	 loadTravelBookings();
    	 loadCargoBookings();
         // Listener for Travel Booking ChoiceBox
         allTravelBookingCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue != null) {
                 onTravelBookingSelected(newValue);
             }
         });

         // Listener for Cargo Booking ChoiceBox
         allCargoTravelBookingCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue != null) {
                 onCargoBookingSelected(newValue);
             }
         });
     }

    private void loadTravelBookings() {
        Map<Integer, String> travelBookings = dbBookingHandler.getAllTravelBookingsForUser(userID); // Replace 1 with logged-in user ID
        ObservableList<Integer> travelBookingIDs = FXCollections.observableArrayList(travelBookings.keySet());
        allTravelBookingCB.setItems(travelBookingIDs);
    }

    private void loadCargoBookings() {
        Map<Integer, String> cargoBookings = dbBookingHandler.getAllCargoBookingsForUser(userID); // Replace 1 with logged-in user ID
        ObservableList<Integer> cargoBookingIDs = FXCollections.observableArrayList(cargoBookings.keySet());
        allCargoTravelBookingCB.setItems(cargoBookingIDs);
    }

    // Populate travel booking fields when a booking is selected
    private void onTravelBookingSelected(Integer bookingID) {
        if (bookingID != null) {
            Map<String, String> bookingDetails = dbBookingHandler.getTravelBookingDetails(bookingID);
            if (bookingDetails != null) {
                sourceTB.setText(bookingDetails.getOrDefault("origin", ""));
                destinationTb.setText(bookingDetails.getOrDefault("destination", ""));
                dateTB.setText(bookingDetails.getOrDefault("bookingDate", ""));
            } else {
                System.out.println("No details found for booking ID: " + bookingID);
            }
        } else {
            System.out.println("No travel booking selected.");
        }
    }

    private void onCargoBookingSelected(Integer cargoBookingID) {
        if (cargoBookingID != null) {
            Map<String, String> bookingDetails = dbBookingHandler.getCargoBookingDetails(cargoBookingID);
            if (bookingDetails != null) {
                cargoSourceTB.setText(bookingDetails.getOrDefault("origin", ""));
                cargoDestinationTb.setText(bookingDetails.getOrDefault("destination", ""));
                cargoDateTB.setText(bookingDetails.getOrDefault("bookingDate", ""));
            } else {
                System.out.println("No details found for cargo booking ID: " + cargoBookingID);
            }
        } else {
            System.out.println("No cargo booking selected.");
        }
    }

    // Commit changes for travel bookings
    @FXML
    private void commitTravelChanges() {
        Integer bookingID = allTravelBookingCB.getValue();
        LocalDate newDate = travelDatePicker.getValue();

        if (bookingID != null && newDate != null) {
        	dbBookingHandler.updateTravelBookingDate(bookingID, newDate);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Booking date updated successfully.");
            loadTravelBookings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a booking and a valid date.");
        }
    }

    // Commit changes for cargo bookings
    @FXML
    private void commitCargoChanges() {
        Integer cargoBookingID = allCargoTravelBookingCB.getValue();
        LocalDate newDate = cargoDatePicker.getValue();

        if (cargoBookingID != null && newDate != null) {
        	dbBookingHandler.updateCargoBookingDate(cargoBookingID, newDate);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Cargo booking date updated successfully.");
            loadCargoBookings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a booking and a valid date.");
        }
    }

    // Cancel travel booking
    @FXML
    private void cancelTravelBooking() {
        Integer bookingID = allTravelBookingCB.getValue();
        if (bookingID != null) {
        	dbBookingHandler.deleteTravelBooking(bookingID);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Booking canceled successfully.");
            loadTravelBookings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a booking to cancel.");
        }
    }

    // Cancel cargo booking
    @FXML
    private void cancelCargoBooking() {
        Integer cargoBookingID = allCargoTravelBookingCB.getValue();
        if (cargoBookingID != null) {
        	dbBookingHandler.deleteCargoBooking(cargoBookingID);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Cargo booking canceled successfully.");
            loadCargoBookings();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a booking to cancel.");
        }
    }

    // Utility method for showing alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    public void goToMainDashboard(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("/fxmlFiles/mainDashboard.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading mainDashboard.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

