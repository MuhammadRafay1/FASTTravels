package application;

import java.io.IOException;

import Classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegistrationController {
    @FXML private TextField usernameTb;
    @FXML private TextField emailTb;
    @FXML private TextField passwordTb;
    @FXML private TextField cnictb;
    @FXML private TextField phonenumtb;

    private DatabaseHandler dbHandler = new DatabaseHandler();
    private User user;
    @FXML
    private void handleRegistration(ActionEvent event) {
        user.setName(usernameTb.getText());
        user.setPassword(passwordTb.getText());
        user.setEmail(emailTb.getText());
        user.setCnic(cnictb.getText());
        user.setPhone(phonenumtb.getText());
        
        if (dbHandler.registerUser(user)) {
            showAlert("Registration Successful", "Navigate to login Page");
        } else {
            showAlert(" Failed", "Incorrect credentials or Account already exists.");
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("login.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading login.fxml: " + e.getMessage());
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
