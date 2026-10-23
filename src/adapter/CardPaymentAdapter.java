package adapter;

public class CardPaymentAdapter implements PaymentProcessor {
    private ExternalPaymentSystem externalSystem;
    private String cardNumber;

    public CardPaymentAdapter(ExternalPaymentSystem externalSystem, String cardNumber) {
        this.externalSystem = externalSystem;
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment(double amount) {
        int cents = (int) Math.round(amount * 100);
        return externalSystem.makePayment(cardNumber, cents);
    }

    @Override
    public String getPaymentMethod() {
        return "Bank card";
    }
}
