package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


    @FXML
    public void initialize() {
        System.out.println("PaymentController: initialize() called.");

        loadBookingDetails(); // Load the current booking details
        setupPaymentMethods(); // Add payment methods to the dropdown
    }

 
    private void loadBookingDetails() {
        bookingID = SessionManager.getInstance().getBookingID(); // Retrieve booking ID from session
        System.out.println("Loading details for bookingID: " + bookingID);

        String query = "SELECT fare FROM Booking WHERE bookingID = ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingID); // Set the Booking ID
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bookingidtb.setText(String.valueOf(bookingID)); // Set booking ID in the TextField
                ammounttb.setText(String.format("%.2f", rs.getFloat("fare"))); // Set the fare amount
                System.out.println("Fare: " + rs.getFloat("fare"));
            } else {
                System.out.println("No booking found for bookingID: " + bookingID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load booking details: " + e.getMessage());
        }
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
        String query = """
                INSERT INTO Payment (bookingID, amount, status, paymentType)
                VALUES (?, ?, 'Unpaid', 'Cash')
                """;

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingID); // Set the Booking ID
            stmt.setFloat(2, Float.parseFloat(ammounttb.getText())); // Set the Payment Amount

            stmt.executeUpdate(); // Execute the query

            showAlert("Payment Method: Cash", "Pay on arrival. Your payment is marked as pending.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to process payment: " + e.getMessage());
        }
    }


    /**
     * Handles the card payment option by navigating to the card payment screen.
     * This method can be called either from a key press or another function.
     */
    private void handleCardPayment() {
        try {
            // Load the Card Payment Screen (CardPayment.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CardPayment.fxml"));
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
