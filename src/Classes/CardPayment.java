package Classes;


//CardPayment.java - Concrete Class for Card Payments
public class CardPayment extends Payment {
 private String cardNumber;
 private String expiryDate;
 private String cvv;

 public CardPayment(int paymentID, int bookingID, float amount) {
     super(paymentID,bookingID,amount);
     this.cardNumber = cardNumber;
     this.expiryDate = expiryDate;
     this.cvv = cvv;
 }

 @Override
 public boolean processPayment() {
     System.out.println("Processing card payment of $" + amount);
     // Additional logic for validating and processing card payment
	return true;
 }

 // Optional getters and setters for card details
}