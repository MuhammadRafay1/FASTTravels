
package application;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Classes.User;

public class LoginController {
    @FXML private TextField usernameTb;
    @FXML private TextField passwordTb;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private DatabaseHandler dbHandler = new DatabaseHandler();
    public static int userID;  // To store the userID
   
    
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameTb.getText();
        String password = passwordTb.getText();
        // Call validateLogin to get both userID and role
        int[] array = dbHandler.validateLogin(username, password);  // Get userID and role
        int userID = array[0];  // Get userID from the array
        int role = array[1];  // Get role (0 for customer, 1 for admin
        SessionManager.getInstance().setUserID(userID); // Example userID
        if (userID != -1) {  // If userID is valid (not -1), login successful
            try {
                if (role == 1) {  // Admin
                    goToAdminDashboard(event);
                } else {  // Customer
                    goToMainDashboard(event);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Login Failed", "Incorrect username or password.");
        }
    }


    @FXML
    public void switchToRegisterPage(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("registration.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading registration.fxml: " + e.getMessage());
            e.printStackTrace();
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
    public void goToAdminDashboard(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AdminDashboard.fxml: " + e.getMessage());
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

