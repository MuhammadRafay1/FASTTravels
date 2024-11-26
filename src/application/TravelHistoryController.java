package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import databaseControllers.userDatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TravelHistoryController {

    @FXML
    private TextArea travelHistoryTA; // Changed to TextArea for multiline support

    @FXML
    private CheckBox fbdCb; // "Filter by Date" Checkbox

    @FXML
    private Button searchButton; // Search Button

    /**
     * Fetch and display travel history for the logged-in user.
     */
    public void showTravelHistory(ActionEvent event) {
        int userID = SessionManager.getInstance().getUserID(); // Get userID from session or login context

        // Call the database handler to fetch travel history
        userDatabaseHandler dbHandler = new userDatabaseHandler();
        List<String> travelHistory = dbHandler.getTravelHistory(userID, fbdCb.isSelected());

        // Display travel history in the TextArea
        if (travelHistory.isEmpty()) {
            travelHistoryTA.setText("No travel history found.");
        } else {
            travelHistoryTA.setText(String.join("\n", travelHistory)); // Combine list into a single string
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
