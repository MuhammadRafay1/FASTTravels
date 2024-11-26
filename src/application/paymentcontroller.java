package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import databaseControllers.BookingDatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class paymentcontroller {

    @FXML
    private TextField ammounttb; // TextField for Amount

    @FXML
    private Button backbutton; // Back Button

    @FXML
    private TextField bookingidtb; // TextField for Booking ID

    @FXML
    private MenuButton choictb; // MenuButton for Payment Method

    @FXML
    private Button proceedbutton; // Button to Confirm Payment Method

    private int bookingID; // Holds the current Booking ID
    private float fare;
    BookingDatabaseHandler dbhandler = new BookingDatabaseHandler();
    @FXML
    public void initialize() {
        System.out.println("PaymentController: initialize() called.");

        loadBookingDetails(); // Load the current booking details
        setupPaymentMethods(); // Add payment methods to the dropdown
    }

 
    private void loadBookingDetails() {
        bookingID = SessionManager.getInstance().getBookingID(); // Retrieve booking ID from session
        System.out.println("Loading details for bookingID: " + bookingID);
        fare = dbhandler.loadBookingDetailsWithBookingID(bookingID);
        bookingidtb.setText(String.valueOf(bookingID)); // Set booking ID in the TextField
        ammounttb.setText(String.format("%.2f", fare)); // Set the fare amount
    }

    private void setupPaymentMethods() {
        // Cash Payment Option
        MenuItem cashPayment = new MenuItem("Cash Payment");
        cashPayment.setOnAction(e -> handleCashPayment());

        // Card Payment Option
        MenuItem cardPayment = new MenuItem("Card Payment");
        cardPayment.setOnAction(e -> handleCardPayment());

        // Add options to the MenuButton
        choictb.getItems().addAll(cashPayment, cardPayment);
    }


    private void handleCashPayment() {
        dbhandler.insertingToPayment(bookingID, fare);
    }


    /**
     * Handles the card payment option by navigating to the card payment screen.
     * This method can be called either from a key press or another function.
     */
    private void handleCardPayment() {
        try {
            // Load the Card Payment Screen (CardPayment.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/CardPayment.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage currentStage;

            if (proceedbutton != null && proceedbutton.getScene() != null) {
                // Retrieve the stage from the button if called via key press
                currentStage = (Stage) proceedbutton.getScene().getWindow();
            } else {
                // Fallback: Create a new stage or handle it gracefully
                System.out.println("No UI reference available. Creating a new stage.");
                currentStage = new Stage();
            }

            // Set the scene and show the Card Payment screen
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Card Payment");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Card Payment screen: " + e.getMessage());
        }
    }

    
    public void proceedToCardPayment(ActionEvent event) {
        SessionManager.getInstance().setBookingID(bookingID); // Example booking ID
        handleCardPayment();
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
