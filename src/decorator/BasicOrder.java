package decorator;

public class BasicOrder implements OrderPrice {
    private double cost;

    public BasicOrder(double cost) {
        this.cost = cost;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public String getDescription() {
        return "Standard order";
    }
}
