package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CardPaymentController {
	@FXML
    private TextField CVVTB;

    @FXML
    private Button backButton;

    @FXML
    private TextField cardHolderNameTB;

    @FXML
    private TextField cardNumberTB;

    @FXML
    private Button confrimPayementButton;
    
    DatabaseHandler dbHandler = new DatabaseHandler();
    @FXML
    public void confirmPayment(ActionEvent event) {
        // Retrieve data from TextFields
        String cardHolderName = cardHolderNameTB.getText().trim();
        String cardNumber = cardNumberTB.getText().trim();
        String cvv = CVVTB.getText().trim();
        int bookingID = SessionManager.getInstance().getBookingID(); // Retrieve bookingID from SessionManager
        float amount = dbHandler.fetchAmountForBooking(bookingID); // Retrieve amount for the booking

        if (cardHolderName.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty()) {
            showAlert("Validation Error", "Please fill all fields.");
            return;
        }

        if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            showAlert("Validation Error", "Card number must be 16 digits.");
            return;
        }

        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
            showAlert("Validation Error", "CVV must be 3 digits.");
            return;
        }
        
        dbHandler.insertIntoCardPayment(cardHolderName,cardNumber,cvv,bookingID,amount);

    }
    
    @FXML
    public void backToPayment(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("Payment.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Payment.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
