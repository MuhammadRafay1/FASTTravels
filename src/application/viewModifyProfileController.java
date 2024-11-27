package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import Classes.User;
import databaseControllers.userDatabaseHandler;
public class viewModifyProfileController {

    @FXML
    private TextField cnicTb;

    @FXML
    private Button commitChangesButton;

    @FXML
    private TextField emailTb;

    @FXML
    private TextField pNumTb;

    @FXML
    private TextField userIDTb;

    @FXML
    private TextField usernameTB;

    @FXML
    private Button viewSavedInfoButton;

    private int currentUserID;

    private userDatabaseHandler userdbHandler = new userDatabaseHandler();

    // Method to populate the form with user data
    public void loadUserData(ActionEvent event) throws IOException {
        currentUserID = SessionManager.getInstance().getUserID();

        // Fetch user data from DatabaseHandler
        User user = userdbHandler.getUserByID(currentUserID);
        if (user != null) {
            populateFields(user);
        } else {
            showAlert("Error", "Failed to load user data.");
        }
    }

    // Method to save user data
    @FXML
    public void onSave(ActionEvent event) {
        User user = new User();
        user.setUserID(currentUserID);
        user.setName(usernameTB.getText());
        user.setEmail(emailTb.getText());
        user.setCnic(cnicTb.getText());
        user.setPhone(pNumTb.getText());

        boolean isUpdated = user.updateUser(user);
        if (isUpdated) {
            showAlert("Success", "User data updated successfully!");
        } else {
            showAlert("Error", "Failed to update user data.");
        }
    }

    // Navigate to the main dashboard
    @FXML
    public void goToMainDashboard(ActionEvent event) throws IOException {
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("/fxmlFiles/mainDashboard.fxml"));
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
            AnchorPane root = FXMLLoader.load(getClass().getResource("/fxmlFiles/AdminDashboard.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading AdminDashboard.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Utility method to populate fields with user data
    private void populateFields(User user) {
        userIDTb.setText(String.valueOf(user.getUserID()));
        usernameTB.setText(user.getName());
        emailTb.setText(user.getEmail());
        cnicTb.setText(user.getCnic());
        pNumTb.setText(user.getPhone());
    }

    // Utility method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
