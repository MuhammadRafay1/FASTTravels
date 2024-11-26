package Classes;

import java.util.List;

public class Admin extends User {

    public Admin(int userID, String name, String email, String phone, String cnic,String password) {
        super(userID, name, email, cnic,phone, password, "Admin");
    }



//    public void manageRoutes(List<Route> routes) {
//        System.out.println("Managing routes.");
//        routes.forEach(Route::viewRoute);
//    }
//
//    public void manageVehicles(List<Vehicle> vehicles) {
//        System.out.println("Managing vehicles.");
//        vehicles.forEach(Vehicle::viewVehicleDetails);
//    }
//
//    public void viewComplaints(List<Complaint> complaints) {
//        System.out.println("Viewing complaints:");
//        complaints.forEach(complaint -> System.out.println(complaint.viewComplaintStatus()));
//    }
//
//    public void manageDiscounts(List<Discount> discounts) {
//        System.out.println("Managing discounts.");
//        discounts.forEach(discount -> System.out.println("Discount ID: " + discount.getDiscountID()));
//    }
}
