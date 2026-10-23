package strategy;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscount(double total, int itemCount) {
        return 0;
    }

    @Override
    public String getName() {
        return "No discount";
    }
}
