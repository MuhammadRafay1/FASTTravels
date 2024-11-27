package Classes;

import java.sql.SQLException;
import java.util.regex.Pattern;
import databaseControllers.*;
public class User {
    protected int userID;
    protected String name;
    protected String email;
    protected String password;
    protected String cnic;
    protected String phone;
    protected String type;
    private userDatabaseHandler userDbHandler = new userDatabaseHandler();
    public User(int userID, String name, String email, String password, String cnic, String phone, String type) {
        this.userID = userID;
        this.name = name;
        setEmail(email); // Using validation
        setPassword(password); // Using validation
        setCnic(cnic); // Using validation
        setPhone(phone); // Using validation
        this.type = type;
    }
    
    public User(int userID, String name, String email, String cnic, String phone) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.cnic = cnic;
        this.phone = phone;
    }
    
    public User() {}

    public User(int userID2) {
    	this.userID = userID2;
		// TODO Auto-generated constructor stub
	}

	// Login method
    public void login() {
        System.out.println(name + " logged in.");
    }


    // Validate email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    // Validate password (at least 8 characters, 1 uppercase, 1 lowercase, 1 number)
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    // Validate CNIC (Pakistan CNIC format: 13 digits)
    private boolean isValidCnic(String cnic) {
        String cnicRegex = "^\\d{13}$";
        return Pattern.matches(cnicRegex, cnic);
    }

    // Validate phone number (Pakistan phone format: starts with +92 or 03, 11 digits)
    private boolean isValidPhone(String phone) {
        String phoneRegex = "^(\\+92|03)\\d{9}$";
        return Pattern.matches(phoneRegex, phone);
    }

    // --- Getters and Setters with Validation ---

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {

        this.cnic = cnic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public User getUserByID(int userID) {
		return userDbHandler.getUserByID(userID);
    	
    }
    
    public boolean updateUser(User user) {
    	return userDbHandler.updateUser(user);
    }
    
    public double getWalletBalance() throws SQLException {
        return userDbHandler.getWalletBalance(this.userID);
    }

    // Update the wallet balance for this user
    public void updateWalletBalance(double newBalance) throws SQLException {
    	userDbHandler.updateWalletBalance(this.userID, newBalance);
    }
    
    public int getLoyaltyPoints() throws SQLException {
        return userDbHandler.getLoyaltyPoints(this.userID);
    }

    // Increase loyalty points for the user
    public void increaseLoyaltyPoints(int points) throws SQLException {
    	userDbHandler.increaseLoyaltyPoints(this.userID, points);
    }
}
