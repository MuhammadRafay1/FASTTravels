package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainDashboardController {
	
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
    
    @FXML
    public void goToBookTickets(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("bookTicekts.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading bookTicekts.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void goToComplain(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("UserSubmitcomplaints.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading UserSubmitcomplaints.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void goToViewModifyProfile(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("viewModifyProfile.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading viewModifyProfile.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void goToTravel(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("TravelHistory.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading TravelHistory.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void goToLoyaltyPoints(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("LoyaltyPoints.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading LoyaltyPoints.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    @FXML
    public void goToCargoBooking(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("cargoBooking.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading cargoBooking.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void goToPreferences(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("Preference.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading Preference.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    public void goToModifyBooking(ActionEvent event) throws IOException {
        try {
            // Load the RegisterPage.fxml
            AnchorPane root = FXMLLoader.load(getClass().getResource("modifyBooking.fxml"));
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading modifyBooking.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    


}
