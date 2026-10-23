package decorator;

public class GiftWrapDecorator extends OrderDecorator {
    public GiftWrapDecorator(OrderPrice order) {
        super(order);
    }

    @Override
    public double getCost() {
        return order.getCost() + 5;
    }

    @Override
    public String getDescription() {
        return order.getDescription() + ", gift wrap";
    }
}
