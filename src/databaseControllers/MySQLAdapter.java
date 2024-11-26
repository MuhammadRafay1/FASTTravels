package databaseControllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;

public class MySQLAdapter implements DatabaseAdapter {
    private final static String URL = "jdbc:mysql://localhost:3306/fasttravelsdatabase";
    private final static String USER = "root";
    private final static String PASSWORD = "123456";

    @Override
    public Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("MySQL Database connected successfully!");
            return conn;
        } catch (SQLException e) {
            System.out.println("Failed to connect to the MySQL database!");
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
