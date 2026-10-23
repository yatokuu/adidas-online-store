package adapter;

public class WalletPayment implements PaymentProcessor {
    private String walletName;

    public WalletPayment(String walletName) {
        this.walletName = walletName;
    }

    @Override
    public boolean processPayment(double amount) {
        if (walletName == null || walletName.isBlank() || walletName.equalsIgnoreCase("blocked")) {
            return false;
        }
        System.out.println("Wallet payment accepted: $" + String.format("%.2f", amount));
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Electronic wallet";
    }
}
