package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import databaseControllers.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TravelPreferenceController {
	
	    @FXML
	    private Button CPButton;

	    @FXML
	    private MenuButton DPChoice;

	    @FXML
	    private TextField PreferredLanguagesTB;

	    @FXML
	    private TextField SeatPreferenceTB;

	    @FXML
	    private CheckBox TickBoxHandicapped;

	    @FXML
	    private Button backtoMenuButton;
	    
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
	    
	    public void insertPreferences(int userID) throws IOException {
	        String dietaryPreference = DPChoice.getText(); // Selected dietary preference
	        boolean isHandicapped = TickBoxHandicapped.isSelected(); // Handicapped status
	        String preferredLanguage = PreferredLanguagesTB.getText().trim(); // Preferred language
	        String seatPreference = SeatPreferenceTB.getText().trim(); // Seat preference

	        String query = """
	                INSERT INTO UserPreferences (userID, dietaryPreference, isHandicapped, preferredLanguage, seatPreference)
	                VALUES (?, ?, ?, ?, ?)
	                ON DUPLICATE KEY UPDATE
	                dietaryPreference = VALUES(dietaryPreference),
	                isHandicapped = VALUES(isHandicapped),
	                preferredLanguage = VALUES(preferredLanguage),
	                seatPreference = VALUES(seatPreference);
	                """;

	        try (Connection conn = DatabaseHandler.connect(); // Establish connection
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            // Bind parameters
	            stmt.setInt(1, userID);
	            stmt.setString(2, dietaryPreference);
	            stmt.setBoolean(3, isHandicapped);
	            stmt.setString(4, preferredLanguage);
	            stmt.setString(5, seatPreference);

	            // Execute the query
	            int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Preferences saved successfully.");
	            } else {
	                System.out.println("Failed to save preferences.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error saving preferences: " + e.getMessage());
	        }
	    }
	    
	    public void onConfirmPreferences(ActionEvent event) throws IOException {
	        int userID = SessionManager.getInstance().getUserID(); // Replace with the actual user ID (e.g., from a session manager)
	        String[] dietaryPreferences = {"Vegetarian", "Vegan", "Halal", "Kosher", "Gluten-Free", "No Preference"};

	        // Add each preference as a MenuItem
	        for (String preference : dietaryPreferences) {
	            MenuItem item = new MenuItem(preference);

	            // Set an action for each MenuItem
	            item.setOnAction(e -> DPChoice.setText(preference)); // Update the MenuButton text when selected

	            // Add the MenuItem to the MenuButton
	            DPChoice.getItems().add(item);
	        }
	        insertPreferences(userID);
	    }


}
