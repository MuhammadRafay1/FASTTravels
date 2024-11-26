package application;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import Classes.*;
import databaseControllers.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class BookTicketsController {

    @FXML private ChoiceBox<Vehicle> avaialableTrains;
    @FXML private ChoiceBox<Vehicle> availabalebus;
    @FXML private ChoiceBox<Vehicle> availableflights;

    @FXML private DatePicker busDate;
    @FXML private TextField busSourcetb;
    @FXML private TextField busDesttb;
    @FXML private TextField busNumTickets;

    @FXML private DatePicker flightdate;
    @FXML private TextField flightSourcetb;
    @FXML private TextField flightDesttb;
    @FXML private TextField flightNumTickets;

    @FXML private DatePicker trainDate;
    @FXML private TextField trainSourcetb;
    @FXML private TextField trainDestinationtb;
    @FXML private TextField trainNumTickets;

    @FXML private Button cbBus;
    @FXML private Button cbFlight;
    @FXML private Button cbTrain;
    @FXML private Button bsearch;
    @FXML private Button fsearch;
    @FXML private Button tsearch;
    @FXML private Button goToMainDashboard;

    @FXML private Tab bussearch;
    @FXML private Tab flightSearch;
    @FXML private Tab trainSearch;

    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final BookingDatabaseHandler dbBookingHandler = new BookingDatabaseHandler();
    private final VehicleRouteDatabaseHandler dbVehicleRouteHandler = new VehicleRouteDatabaseHandler();
    private Vehicle selectedVehicle;
    private Booking currentBooking;
    int numTickets;
    
    @FXML
    public void initialize() {
        LocalDate today = LocalDate.now();
        busDate.setDayCellFactory(picker -> createDayCell(today));
        trainDate.setDayCellFactory(picker -> createDayCell(today));
        flightdate.setDayCellFactory(picker -> createDayCell(today));
    }

    private DateCell createDayCell(LocalDate today) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(today) || item.isAfter(today.plusDays(7)));
            }
        };
    }
    @FXML
    public void confirmBooking(ActionEvent event) throws SQLException {
        if (currentBooking == null || selectedVehicle == null) {
            showAlert("Error", "No booking or vehicle selected.");
            return;
        }

        LocalDate date = currentBooking.getDate();

        // Check if the date is within the next 7 days
        if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusDays(7))) {
            showAlert("Error", "Bookings can only be made for the next 7 days.");
            return;
        }

        int availableSeats = dbVehicleRouteHandler.getAvailableSeats(selectedVehicle.getVehicleID(), date);
        if (availableSeats == -1) {
            // If no entry exists, initialize with max capacity and subtract tickets
            int maxCapacity = dbVehicleRouteHandler.getMaxVehicleCapacity(selectedVehicle.getVehicleID());
            availableSeats = maxCapacity - currentBooking.getNumTickets();
            dbVehicleRouteHandler.insertVehicleCapacity(selectedVehicle.getVehicleID(), date, availableSeats);
        } else if (availableSeats >= currentBooking.getNumTickets()) {
            // Update capacity if enough seats are available
            dbVehicleRouteHandler.updateVehicleCapacity(selectedVehicle.getVehicleID(), date, -currentBooking.getNumTickets());
        } else {
            // Offer waitlist if insufficient seats
            boolean addToWaitlist = showWaitlistPrompt();
            if (addToWaitlist) {
                dbVehicleRouteHandler.addToWaitlist(
                    SessionManager.getInstance().getUserID(),
                    selectedVehicle.getVehicleID(),
                    date,
                    currentBooking.getNumTickets()
                );
                showAlert("Added to Waitlist", "You have been added to the waitlist for this booking.");
            } else {
                showAlert("Booking Failed", "Not enough seats available and waitlist declined.");
            }
            return;
        }

        // Confirm booking
        double distance = dbVehicleRouteHandler.getRouteDistance(selectedVehicle.getVehicleID());
        currentBooking.setFare(currentBooking.calculatePrice(distance, currentBooking.getNumTickets()));
        int bookingID = dbBookingHandler.insertBooking(
            selectedVehicle.getVehicleID(),
            currentBooking.getDate(),
            currentBooking.getNumTickets(),
            currentBooking.getRoute().getStartPoint(),
            currentBooking.getRoute().getEndPoint(),
            currentBooking.getBookingType(),
            SessionManager.getInstance().getUserID(),
            currentBooking.getFare()
        );

        showAlert("Booking Confirmed", "Fare: " + currentBooking.getFare() + "\nBooking ID: " + bookingID);
    }


    @FXML
    public void populateFlightChoiceBox() {
        populateChoiceBox(flightSourcetb, flightDesttb, flightdate, flightNumTickets, "Flight", availableflights);
    }

    @FXML
    public void populateBusChoiceBox() {
        populateChoiceBox(busSourcetb, busDesttb, busDate, busNumTickets, "Bus", availabalebus);
    }

    @FXML
    public void populateTrainChoiceBox() {
        populateChoiceBox(trainSourcetb, trainDestinationtb, trainDate, trainNumTickets, "Train", avaialableTrains);
    }

    private void populateChoiceBox(TextField sourceField, TextField destinationField, DatePicker datePicker, TextField ticketsField, String type, ChoiceBox<Vehicle> choiceBox) {
        try {
            // Validate inputs
            if (sourceField.getText().isEmpty() || destinationField.getText().isEmpty() || datePicker.getValue() == null || ticketsField.getText().isEmpty()) {
                showAlert("Validation Error", "Please fill all fields.");
                return;
            }

            // Create Route
            Route route = new Route(sourceField.getText(), destinationField.getText());
            numTickets = Integer.parseInt(ticketsField.getText());
            LocalDate date = datePicker.getValue();

            // Use Factory to create Booking instance
            currentBooking = BookingFactory.createBooking(type, route, numTickets, date);

            // Fetch available vehicles
            List<Vehicle> vehicles = dbVehicleRouteHandler.getAvailableVehiclesByRouteAndType(route.getStartPoint(), route.getEndPoint(), type);
            choiceBox.getItems().clear();
            choiceBox.getItems().addAll(vehicles);

            // Listener for vehicle selection
            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedVehicle = newValue);

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid number of tickets.");
        } catch (SQLException e) {
            showAlert("Error", "Failed to load vehicles: " + e.getMessage());
        }
    }

    @FXML
    public void goToMainDashboard(ActionEvent event) throws IOException {
        switchScene("/fxmlFiles/mainDashboard.fxml", event);
    }

    @FXML
    public void proceedToPayment(ActionEvent event) throws IOException {
        switchScene("/fxmlFiles/Payment.fxml", event);
    }

    private void switchScene(String fxmlPath, ActionEvent event) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean showWaitlistPrompt() {
        // Create a confirmation alert
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Waitlist Confirmation");
        alert.setHeaderText("Vehicle is Fully Booked");
        alert.setContentText("Would you like to be added to the waitlist?");

        // Show the alert and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // Return true if the user clicks "Yes", false otherwise
        return result.isPresent() && result.get() == ButtonType.OK;
    }

}
