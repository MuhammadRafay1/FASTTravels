package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserSubmitComplaintsController {
	
	 @FXML
	    private Button RegisterButton;

	    @FXML
	    private ImageView avatarLogo;

	    @FXML
	    private TextField detailstb;

	    @FXML
	    private Button goToMenu;

	    @FXML
	    private ImageView imgLogo;

	    @FXML
	    private TextField usernameTb;
	    
	    DatabaseHandler dbhandler = new DatabaseHandler();

    @FXML
    public void goToMenu(ActionEvent event) throws IOException {
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
    private void handleSubmitComplaint(ActionEvent event) {
        String details = detailstb.getText(); // Get details from a TextField
        int userID = SessionManager.getInstance().getUserID();  // Assuming userID is available globally
        int vehicleID = Integer.parseInt(usernameTb.getText()); // Vehicle ID selected by the user (set somewhere in your app)

        // Call the method to insert the complaint into the database
        dbhandler.insertComplaint(userID, vehicleID, details);
    }

}
