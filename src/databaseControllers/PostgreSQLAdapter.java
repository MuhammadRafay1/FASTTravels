package databaseControllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class PostgreSQLAdapter implements DatabaseAdapter {
    private final static String URL = "jdbc:postgresql://localhost:5432/fasttravelsdatabase";
    private final static String USER = "postgres";
    private final static String PASSWORD = "123456";

    @Override
    public Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("PostgreSQL Database connected successfully!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Failed to connect to the PostgreSQL database!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
