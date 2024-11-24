package Classes;

import java.util.regex.Pattern;

public abstract class User {
    protected int userID;
    protected String name;
    protected String email;
    protected String password;
    protected String cnic;
    protected String phone;
    protected String type;

    public User(int userID, String name, String email, String password, String cnic, String phone, String type) {
        this.userID = userID;
        this.name = name;
        setEmail(email); // Using validation
        setPassword(password); // Using validation
        setCnic(cnic); // Using validation
        setPhone(phone); // Using validation
        this.type = type;
    }
    
    public User() {}

    // Login method
    public void login() {
        System.out.println(name + " logged in.");
    }

    // Abstract method for profile updates
    public abstract void updateProfile(String newEmail);

    // --- Validation Functions ---

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
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters, include one uppercase, one lowercase, and one number.");
        }
        this.password = password;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        if (!isValidCnic(cnic)) {
            throw new IllegalArgumentException("Invalid CNIC format. CNIC must contain 13 digits.");
        }
        this.cnic = cnic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format. Must start with +92 or 03 and be 11 digits long.");
        }
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
}
