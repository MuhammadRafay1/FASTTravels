package application;

import java.io.IOException;
import databaseControllers.userDatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class loyaltyPointController {
	
	@FXML
    private Button backToMenuButton;

    @FXML
    private Label lpLabel;

    @FXML
    private Button redeemToWalletButton;
    
    userDatabaseHandler userdbHandler = new userDatabaseHandler();
    
    @FXML
    public void initialize() {
        System.out.println("loyaltyPointController: initialize() called.");

        int userID = SessionManager.getInstance().getUserID();
        int loyaltyPoints = userdbHandler.getLoyaltyPoints(userID);
        lpLabel.setText(String.valueOf(loyaltyPoints)); // Convert int to String
    }
    
    @FXML
    public void redeemPoints(ActionEvent event) {
    	int userID = SessionManager.getInstance().getUserID();
    	userdbHandler.redeemLoyaltyPoints(userID);
    	lpLabel.setText(String.valueOf(0));
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
