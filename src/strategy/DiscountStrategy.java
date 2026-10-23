package strategy;

public interface DiscountStrategy {
    double calculateDiscount(double total, int itemCount);
    String getName();
}
