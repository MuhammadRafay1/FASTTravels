package Classes;


public class Complaint {
    private int complaintID;
    private int userID;
    private int vehicleID;
    private String details;
    private String status;

    public Complaint(int complaintID, int userID, String details,int vehicleID) {
        this.complaintID = complaintID;
        this.userID = userID;
        this.details = details;
        this.status = "Submitted";
        this.vehicleID = vehicleID;
    }

    public Complaint() {
		// TODO Auto-generated constructor stub
	}

	public boolean submitComplaint() {
        System.out.println("Complaint submitted: " + details);
        return true;
    }

    public String viewComplaintStatus() {
        return "Complaint Status: " + status;
    }
}

