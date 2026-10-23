package adapter;

public interface PaymentProcessor {
    boolean processPayment(double amount);
    String getPaymentMethod();
}
