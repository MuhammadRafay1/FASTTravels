package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminDashboardController {
	
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
	 public void goToComplaintDashboard(ActionEvent event) throws IOException {
	        try {
	            // Load the RegisterPage.fxml
	            AnchorPane root = FXMLLoader.load(getClass().getResource("AdminManageComplaints.fxml"));
	            
	            // Get the current stage and set the new scene
	            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            System.err.println("Error loading AdminManageComplaints.fxml: " + e.getMessage());
	            e.printStackTrace();
	        }
	 }
	 
	 @FXML
	 public void goToManageVehicle(ActionEvent event) throws IOException {
	        try {
	            // Load the RegisterPage.fxml
	            AnchorPane root = FXMLLoader.load(getClass().getResource("manageVehicle.fxml"));
	            
	            // Get the current stage and set the new scene
	            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            System.err.println("Error loading manageVehicle.fxml: " + e.getMessage());
	            e.printStackTrace();
	        }
	 }
	 
	 @FXML
	 public void goToRouteManagement(ActionEvent event) throws IOException {
	        try {
	            // Load the RegisterPage.fxml
	            AnchorPane root = FXMLLoader.load(getClass().getResource("AdminManageRoutes.fxml"));
	            
	            // Get the current stage and set the new scene
	            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            System.err.println("Error loading AdminManageRoutes.fxml: " + e.getMessage());
	            e.printStackTrace();
	        }
	 }
	 
	 @FXML
	 public void goToManageProfile(ActionEvent event) throws IOException {
	        try {
	            // Load the RegisterPage.fxml
	            AnchorPane root = FXMLLoader.load(getClass().getResource("AdminModifyAccount.fxml"));
	            
	            // Get the current stage and set the new scene
	            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            System.err.println("Error loading AdminManageRoutes.fxml: " + e.getMessage());
	            e.printStackTrace();
	        }
	 }

}
