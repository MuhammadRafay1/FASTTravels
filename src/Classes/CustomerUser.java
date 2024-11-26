package Classes;


import java.util.ArrayList;
import java.util.List;

public class CustomerUser extends User {
    private String cnic;
    private String phone;
    private int loyaltyPoints;
//    private List<Booking> travelHistory;

    public CustomerUser(int userID, String name, String email, String cnic, String phone,String password) {
        super(userID, name, email,phone,cnic,password,"Customer");
        this.cnic = cnic;
        this.phone = phone;
        this.password = password;
//        this.travelHistory = new ArrayList<>();
    }



    public void updateContact(String newPhone) {
        this.phone = newPhone;
        System.out.println("Contact updated.");
    }

//    public List<Booking> viewTravelHistory() {
//        return travelHistory;
//    }
//
//    public void addBookingToHistory(Booking booking) {
//        travelHistory.add(booking);
//    }
//
//    public void redeemLoyaltyPoints(int points) {
//        if (loyaltyPoints >= points) {
//            loyaltyPoints -= points;
//            System.out.println("Redeemed " + points + " loyalty points.");
//        } else {
//            System.out.println("Insufficient loyalty points.");
//        }
//    }

    public void earnLoyaltyPoints(int points) {
        loyaltyPoints += points;
        System.out.println("Earned " + points + " loyalty points.");
    }
}
