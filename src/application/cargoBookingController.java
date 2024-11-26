package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import Classes.RouteDetails;
import Classes.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import databaseControllers.BookingDatabaseHandler;
public class cargoBookingController {
	
	    @FXML
	    private TextField ToCityTB;

	    @FXML
	    private ChoiceBox<Vehicle> avaialabeBussesCB;

	    @FXML
	    private Button confirmBooking;

	    @FXML
	    private DatePicker datePicker;

	    @FXML
	    private TextField fromCityTB;

	    @FXML
	    private Button goToMainDashboard;

	    @FXML
	    private Label headingLabel;

	    @FXML
	    private TextField heightTB;

	    @FXML
	    private Button proceedtoPaymentButoon;

	    @FXML
	    private TextField weightTB;

	    @FXML
	    private TextField widthTB;
	    
	    @FXML
	    private Button searchButton;
	    
	    public int userID;
	    public int vehicleID;
	    public String vehiclename;
	    public String origin,Destination,bookingType;
	    public LocalDate date;
	    public int numTickets;
	    BookingDatabaseHandler dbHandler = new BookingDatabaseHandler();
	    
	    
	    @FXML
	    public void confirmBooking(ActionEvent event) {
	        try {
	            // Validate cargo dimensions and weight
	            int weight = Integer.parseInt(weightTB.getText());
	            int height = Integer.parseInt(heightTB.getText());
	            int width = Integer.parseInt(widthTB.getText());

	            if (weight > 12 || height > 24 || width > 36) {
	                showAlert("Invalid Cargo Dimensions", 
	                          "Weight must be ≤ 12kg, height ≤ 24 inches, and width ≤ 36 inches.");
	                return;
	            }

	            // Fetch route details and vehicle type
	            RouteDetails routeDetails = dbHandler.getRouteDetailsByVehicleID(vehicleID);

	            if (routeDetails != null) {
	                // Calculate fare
	                double fare = calculateFare(routeDetails.getDistance(), weight, routeDetails.getType());

	                // Insert booking and retrieve booking ID
	                int bookingID = dbHandler.insertBooking(vehicleID, date, numTickets, origin, Destination, bookingType, userID, fare);

	                if (bookingID > 0) {
	                    // Insert into CargoBooking table
	                    boolean isCargoInserted = dbHandler.insertCargoBooking(bookingID, userID, vehicleID, weight, height, width, date);

	                    if (isCargoInserted) {
	                        showAlert("Booking Confirmed", "Cargo booking confirmed. Fare: " + fare);
	                    } else {
	                        showAlert("Booking Failed", "Cargo booking insertion failed.");
	                    }
	                } else {
	                    showAlert("Booking Failed", "Could not confirm the booking. Please try again.");
	                }
	            } else {
	                showAlert("No Vehicle Found", "No vehicles found for the selected route.");
	            }
	        } catch (NumberFormatException e) {
	            showAlert("Invalid Input", "Please enter valid numbers for weight, height, and width.");
	        } catch (Exception e) {
	            e.printStackTrace();
	            showAlert("Error", "An error occurred: " + e.getMessage());
	        }
	    }


	    
	    private double calculateFare(double distance, int weight, String type) {
	        double fare = 0.0;
	        fare = weight*5 + distance;
	        return fare;
	    }
	    
	    private void populateChoiceBox(ChoiceBox<Vehicle> choiceBox, String query, String startPoint, String endPoint) {
	        try (Connection conn = dbHandler.connect();
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            stmt.setString(1, startPoint);
	            stmt.setString(2, endPoint);

	            ResultSet rs = stmt.executeQuery();

	            // Create a list to store Vehicle objects
	            ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

	            while (rs.next()) {
	                int vehicleID1 = rs.getInt("vehicleID");
	                String company = rs.getString("company");

	                // Create a Vehicle object and add it to the list
	                vehicleList.add(new Vehicle(vehicleID1, company));
	            }

	            // Populate the ChoiceBox with Vehicle objects
	            choiceBox.setItems(vehicleList);

	            // Add listener to capture user selection
	            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	                if (newValue != null) {
	                    // Access the selected Vehicle object directly
	                    vehicleID = newValue.getVehicleID();
	                    vehiclename = newValue.getCompany();

	                    userID = SessionManager.getInstance().getUserID(); // Or use UserSession.getInstance().getUserID();

	                    System.out.println("Selected Vehicle: " + vehicleID + ", Company: " + vehiclename);

	                    // Perform additional actions like inserting the booking into the database
	                }
	            });

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    @FXML
	    public void populateChoiceBoxUtility(ActionEvent event) {
	        String query = "SELECT v.vehicleID, v.company, r.distance, v.type FROM route r "
	                + "JOIN vehicle v ON r.routeID = v.routeID "
	                + "WHERE r.startPoint = ? AND r.endPoint = ? AND v.type = 'Bus'";

	        origin = fromCityTB.getText();
	        Destination = ToCityTB.getText();
	        date = datePicker.getValue();
	        numTickets = 1;
	        bookingType = "Cargo";
	        populateChoiceBox(avaialabeBussesCB, query, origin, Destination);
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
	    
	    @FXML
	    public void goToPayment(ActionEvent event) throws IOException {
	        try {
	            // Load the RegisterPage.fxml
	        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/Payment.fxml"));
	            Parent root = loader.load();

	            // Pass the bookingID to the controller's initialize method
	            
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
	    
	    public void proceedToPayment(ActionEvent event) {
	        try {
	            // Set the bookingID in the SessionManager
	            //SessionManager.getInstance().setBookingID(bookingID);

	            // Load the Payment Screen FXML
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlFiles/Payment.fxml"));
	            Parent root = loader.load();

	            // The initialize method in PaymentController is automatically called here

	            // Show the Payment Screen
	            Stage stage = new Stage();
	            stage.setTitle("Payment Processing");
	            stage.setScene(new Scene(root));
	            stage.show();

	        } catch (Exception e) {
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
