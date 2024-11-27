package Classes;

//CashPayment.java - Concrete Class for Cash Payments
public class CashPayment extends Payment {
 public CashPayment(int paymentID, int bookingID, float amount) {
     super(paymentID, bookingID,amount);
 }

 @Override
 public boolean processPayment() {
     System.out.println("Processing cash payment of $" + amount);
     // Additional logic for handling cash payments, if necessary
	return true;
 }
}
