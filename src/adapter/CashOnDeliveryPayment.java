package adapter;

public class CashOnDeliveryPayment implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Cash payment of $" + String.format("%.2f", amount) + " will be collected on delivery.");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Cash on delivery";
    }
}
