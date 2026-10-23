package decorator;

public class ExpressDeliveryDecorator extends OrderDecorator {
    public ExpressDeliveryDecorator(OrderPrice order) {
        super(order);
    }

    @Override
    public double getCost() {
        return order.getCost() + 15;
    }

    @Override
    public String getDescription() {
        return order.getDescription() + ", express delivery";
    }
}
