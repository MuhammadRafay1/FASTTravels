package Classes;

public abstract class Payment {
    protected int paymentID;
    protected int bookingID;
    protected float amount;
    private String status;

    public Payment(int paymentID, int bookingID, float amount) {
        this.paymentID = paymentID;
        this.bookingID = bookingID;
        this.amount = amount;
        this.status = "Unpaid";
    }

    public boolean processPayment() {
        this.status = "Paid";
        System.out.println("Payment processed.");
        return true;
    }
}
