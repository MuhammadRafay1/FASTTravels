package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Classes.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Classes.Route;

public class BookTicketsController<PaymentController> {

	 @FXML
	    private ChoiceBox<Vehicle> avaialableTrains;

	    @FXML
	    private ChoiceBox<Vehicle> availabalebus;

	    @FXML
	    private ChoiceBox<Vehicle> availableflights;

	    @FXML
	    private Button bsearch;

	    @FXML
	    private DatePicker busDate;

	    @FXML
	    private TextField busDesttb;

	    @FXML
	    private TextField busNumTickets;

	    @FXML
	    private TextField busSourcetb;

	    @FXML
	    private Tab bussearch;

	    @FXML
	    private Button cbBus;

	    @FXML
	    private Button cbFlight;

	    @FXML
	    private Button cbTrain;

	    @FXML
	    private TextField flightDesttb;

	    @FXML
	    private TextField flightNumTickets;

	    @FXML
	    private Tab flightSearch;

	    @FXML
	    private TextField flightSourcetb;

	    @FXML
	    private DatePicker flightdate;

	    @FXML
	    private Button fsearch;

	    @FXML
	    private Button goToMainDashboard;

	    @FXML
	    private DatePicker trainDate;

	    @FXML
	    private TextField trainDestinationtb;

	    @FXML
	    private TextField trainNumTickets;

	    @FXML
	    private Tab trainSearch;

	    @FXML
	    private TextField trainSourcetb;

	    @FXML
	    private Button tsearch;
	    
	    Vehicle vehicle;
	    Route route;
	    int vehicleID;
	    String vehiclename;
	    LocalDate date;
	    String origin;
	    String Destination;
	    int numTickets;
	    String bookingType;
	    double fare;
	    int userID = -1;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    
    
    @FXML
    public void confirmBooking(ActionEvent event) {
        try {

            Vehicle vehicle = dbHandler.getVehicleByID(vehicleID); // Delegate to DatabaseHandler

            if (vehicle != null) {
                double distance = dbHandler.getRouteDistance(vehicleID); // Delegate to DatabaseHandler
                double fare = vehicle.calculateFare(distance, numTickets, vehicle.getType());

                int bookingID = dbHandler.insertBooking(vehicleID, date, numTickets, origin, Destination, bookingType, userID, fare);

                showAlert("Booking Confirmed", "Fare: " + fare + "BookingID: " + bookingID);
            } else {
                showAlert("Oops!", "No vehicle found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during booking confirmation: " + e.getMessage());
            showAlert("Error", "Something went wrong during booking.");
        }
    }



    @FXML
    public void populateFlightChoiceBox() {
        origin = flightSourcetb.getText();
        Destination = flightDesttb.getText();
        date = flightdate.getValue();
        numTickets = Integer.parseInt(flightNumTickets.getText());
        bookingType = "Flight";

        populateChoiceBoxWithVehicles(availableflights, "Flight", origin, Destination);
    }

    @FXML
    public void populateBusChoiceBox() {
        origin = busSourcetb.getText();
        Destination = busDesttb.getText();
        date = busDate.getValue();
        numTickets = Integer.parseInt(busNumTickets.getText());
        bookingType = "Bus";

        populateChoiceBoxWithVehicles(availabalebus, "Bus", origin, Destination);
    }

    @FXML
    public void populateTrainChoiceBox() {
        origin = trainSourcetb.getText();
        Destination = trainDestinationtb.getText();
        date = trainDate.getValue();
        numTickets = Integer.parseInt(trainNumTickets.getText());
        bookingType = "Train";

        populateChoiceBoxWithVehicles(avaialableTrains, "Train", origin, Destination);
    }

    private void populateChoiceBoxWithVehicles(ChoiceBox<Vehicle> choiceBox, String vehicleType, String startPoint, String endPoint) {
        try {
            List<Vehicle> vehicles = dbHandler.getAvailableVehiclesByRouteAndType(startPoint, endPoint, vehicleType);

            // Populate ChoiceBox with Vehicle objects
            choiceBox.getItems().clear();
            choiceBox.getItems().addAll(vehicles);

            // Add listener to capture user selection
            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Access selected Vehicle object directly
                    vehicleID = newValue.getVehicleID();
                    vehiclename = newValue.getCompany();

                    userID = SessionManager.getInstance().getUserID(); // Use session management for user info

                    System.out.println("Selected Vehicle: " + vehicleID + ", Company: " + vehiclename);

                    // Perform any additional actions if needed
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to fetch vehicles: " + e.getMessage());
        }
    }



    
    @FXML
    public void goToMainDashboard(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("mainDashboard.fxml"));
            
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
    
    @FXML
    public void goToPayment(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment.fxml"));
            Parent root = loader.load();

            // Pass the bookingID to the controller's initialize method
            
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
    
    public void proceedToPayment(ActionEvent event) {
        try {
            // Set the bookingID in the SessionManager
            //SessionManager.getInstance().setBookingID(bookingID);

            // Load the Payment Screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Payment.fxml"));
            Parent root = loader.load();

            // The initialize method in PaymentController is automatically called here

            // Show the Payment Screen
            Stage stage = new Stage();
            stage.setTitle("Payment Processing");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
