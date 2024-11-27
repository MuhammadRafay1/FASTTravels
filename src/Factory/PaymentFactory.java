package Factory;
import Classes.*;
public class PaymentFactory {

    public static Payment createPayment(String paymentType, int paymentID, int bookingID, float amount) {
        switch (paymentType.toLowerCase()) {
            case "cash":
                return new CashPayment(paymentID, bookingID, amount);
            case "card":
                return new CardPayment(paymentID, bookingID, amount);
            default:
                throw new IllegalArgumentException("Invalid payment type: " + paymentType);
        }
    }
}