package databaseControllers;

import java.sql.Connection;

public interface DatabaseAdapter {
    Connection connect();
    void showAlert(String title, String message);
}
