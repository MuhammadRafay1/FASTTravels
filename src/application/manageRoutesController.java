package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import databaseControllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class manageRoutesController {
	
	    @FXML
	    private TextArea allRoutesTextArea;

	    @FXML
	    private Button backToMEnuButton;

	    @FXML
	    private Button deleteRouteButton;

	    @FXML
	    private TextField routeDistanceTB;

	    @FXML
	    private TextField routeEndPointTB;

	    @FXML
	    private TextField routeIDtoDeleteTB;

	    @FXML
	    private TextField routeStartPointTB;

	    @FXML
	    private Button saveRouteButton;
	
	    DatabaseHandler dbHandler = new DatabaseHandler();
	    VehicleRouteDatabaseHandler dbVehicleRouteHandler = new VehicleRouteDatabaseHandler();
	    
	    public void initialize() {
	        populateAllRoutes(); // Populate the text area when the scene loads
	    }
	   
	    @FXML
	    void saveRoute(ActionEvent event) throws IOException, SQLException{
	        String startPoint = routeStartPointTB.getText();
	        String endPoint = routeEndPointTB.getText();
	        String distanceText = routeDistanceTB.getText();

	        if (startPoint.isEmpty() || endPoint.isEmpty() || distanceText.isEmpty()) {
	            showAlert("Error", "All fields must be filled to save a route!");
	            return;
	        }

	        try {
	            double distance = Double.parseDouble(distanceText);
	            dbVehicleRouteHandler.insertRoutes(startPoint, endPoint, distance);
	            // Insert route into the database
	            
	        } catch (NumberFormatException e) {
	            showAlert("Error", "Distance must be a valid number!");
	        }
	    }
	    
	    @FXML
	    void populateAllRoutes() {
	        String query = "SELECT * FROM Route";
	        try (Connection conn = dbHandler.connect();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {

	            StringBuilder allRoutes = new StringBuilder("All Routes:\n");
	            while (rs.next()) {
	                int id = rs.getInt("routeID");
	                String startPoint = rs.getString("startPoint");
	                String endPoint = rs.getString("endPoint");
	                double distance = rs.getDouble("distance");

	                allRoutes.append(String.format("Route ID: %d, From: %s, To: %s, Distance: %.2f km\n",
	                        id, startPoint, endPoint, distance));
	            }

	            allRoutesTextArea.setText(allRoutes.toString());
	        } catch (SQLException e) {
	            allRoutesTextArea.setText("Error: Unable to retrieve routes. " + e.getMessage());
	        }
	    }
	    
	    @FXML
	    void deleteRoute(ActionEvent event) {
	        String routeIdText = routeIDtoDeleteTB.getText();

	        if (routeIdText.isEmpty()) {
	            allRoutesTextArea.setText("Error: Enter a route ID to delete.");
	            return;
	        }

	        try {
	            int routeID = Integer.parseInt(routeIdText);
	            dbVehicleRouteHandler.removeRoute(routeID);
	            populateAllRoutes();
	            
	        }  catch (NumberFormatException e) {
	            allRoutesTextArea.setText("Error: Route ID must be a valid number!");
	        }
	    }
	    
	    @FXML
	    public void goToMainDashboard(ActionEvent event) throws IOException {
	        try {
	            // Load the RegisterPage.fxml
	            AnchorPane root = FXMLLoader.load(getClass().getResource("/fxmlFiles/AdminDashboard.fxml"));
	            
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
	        Alert alert = new Alert(Alert.AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
}
