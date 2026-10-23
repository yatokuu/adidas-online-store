package adapter;

// Imagine this class comes from another company. We do not change it.
public class ExternalPaymentSystem {
    public boolean makePayment(String cardNumber, int amountInCents) {
        System.out.println("External system received " + amountInCents + " cents.");
        return cardNumber != null && cardNumber.length() >= 4 && !cardNumber.endsWith("0000");
    }
}
