package strategy;

public class BulkDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscount(double total, int itemCount) {
        if (itemCount >= 3) return total * 0.08;
        return 0;
    }

    @Override
    public String getName() {
        return "Buy 3 or more - 8%";
    }
}
