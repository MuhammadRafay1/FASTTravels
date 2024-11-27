package Classes;

import java.util.List;
import databaseControllers.*;

public class Admin extends User {

	 private userDatabaseHandler dbHandler = new userDatabaseHandler();
	 private VehicleRouteDatabaseHandler dbVRHandler = new VehicleRouteDatabaseHandler();
    public Admin(int userID, String name, String email, String phone, String cnic,String password) {
        super(userID, name, email, cnic,phone, password, "Admin");
    }
    
    
    public String loadComplaints(boolean showAll) {
        return dbHandler.loadComplaints(showAll);
    }
    
    public boolean resolveComplaint(int complaintID,boolean showAll) {
        return dbHandler.resolveComplaints(complaintID,showAll);
    }
    
    public String getAllRoutes() {
        return dbVRHandler.getAllRoutes();
    }

    
    public boolean addVehicle(String companyName, String routeID, String seatingCapacity, String vehicleType) {
        return dbVRHandler.addVehicle(companyName, routeID, seatingCapacity, vehicleType);
    }

    public boolean deleteVehicle(String vehicleID) {
        return dbVRHandler.deleteVehicle(vehicleID);
    }
    
    public boolean addRoute(String startPoint, String endPoint, double distance) {
        return dbVRHandler.insertRoutes(startPoint, endPoint, distance);
    }


    public boolean deleteRoute(int routeID) {
        return dbVRHandler.removeRoute(routeID);
    }


}
