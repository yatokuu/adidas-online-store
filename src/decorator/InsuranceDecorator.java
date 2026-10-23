package decorator;

public class InsuranceDecorator extends OrderDecorator {
    public InsuranceDecorator(OrderPrice order) {
        super(order);
    }

    @Override
    public double getCost() {
        return order.getCost() + 8;
    }

    @Override
    public String getDescription() {
        return order.getDescription() + ", product insurance";
    }
}
