package application;

public class SessionManager {
    private static SessionManager instance;
    private int userID;
    private int bookingID; // Add bookingID to manage current booking

    // Private constructor to prevent instantiation
    private SessionManager() {}

    // Get the singleton instance
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Getter and Setter for userID
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    // Getter and Setter for bookingID
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
}
