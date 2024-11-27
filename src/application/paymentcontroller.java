package application;

import Classes.Payment;
import Factory.PaymentFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class paymentcontroller {

    @FXML
    private TextField ammounttb;

    @FXML
    private Button backbutton;

    @FXML
    private TextField bookingidtb;

    @FXML
    private MenuButton choictb;

    @FXML
    private Button proceedbutton;

    private int bookingID;
    private float fare;

    @FXML
    public void initialize() {
        loadBookingDetails();
        setupPaymentMethods();
    }

    private void loadBookingDetails() {
        bookingID = SessionManager.getInstance().getBookingID();
        System.out.println("Loading details for bookingID: " + bookingID);

        try {
            fare = Payment.getFareByBookingID(bookingID);
            bookingidtb.setText(String.valueOf(bookingID));
            ammounttb.setText(String.format("%.2f", fare));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load booking details: " + e.getMessage());
        }
    }

    private void setupPaymentMethods() {
        MenuItem cashPayment = new MenuItem("Cash Payment");
        cashPayment.setOnAction(e -> handlePayment("cash"));

        MenuItem cardPayment = new MenuItem("Card Payment");
        cardPayment.setOnAction(e -> handlePayment("card"));

        choictb.getItems().addAll(cashPayment, cardPayment);
    }

    private void handlePayment(String paymentType) {
        try {
            // Use factory to create the specific payment object
            Payment payment = PaymentFactory.createPayment(paymentType, 0, bookingID, fare);

            if (payment.processPayment()) {
                payment.savePayment(paymentType);
                showAlert("Success", paymentType + " payment processed successfully.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to process " + paymentType + " payment: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void proceedToCardPayment(ActionEvent event) {
        SessionManager.getInstance().setBookingID(bookingID);
            try {
                // Load the RegisterPage.fxml
                AnchorPane root = FXMLLoader.load(getClass().getResource("/fxmlFiles/cardPayment.fxml"));
                
                // Get the current stage and set the new scene
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println("Error loading cardPayment.fxml: " + e.getMessage());
                e.printStackTrace();
            }
    }
    
    @FXML
    public void goToMainDashboard(ActionEvent event) {
        try {
            // Load the AdminDashboard.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("/fxmlFiles/mainDashboard.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AdminDashboard.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
