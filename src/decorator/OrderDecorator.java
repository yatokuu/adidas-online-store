package decorator;

public abstract class OrderDecorator implements OrderPrice {
    protected OrderPrice order;

    public OrderDecorator(OrderPrice order) {
        this.order = order;
    }
}
