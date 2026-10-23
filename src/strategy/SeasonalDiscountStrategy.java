package strategy;

public class SeasonalDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscount(double total, int itemCount) {
        return total * 0.10;
    }

    @Override
    public String getName() {
        return "Seasonal sale - 10%";
    }
}
