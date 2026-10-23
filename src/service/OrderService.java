package service;

import adapter.PaymentProcessor;
import data.DataManager;
import data.StoreData;
import decorator.*;
import model.*;
import strategy.DiscountCalculator;
import strategy.DiscountStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrderService {
    private StoreData data;
    private DataManager dataManager;
    private CartService cartService;

    public OrderService(StoreData data, DataManager dataManager, CartService cartService) {
        this.data = data;
        this.dataManager = dataManager;
        this.cartService = cartService;
    }

    public Order checkout(int userId, DiscountStrategy strategy, PaymentProcessor payment,
                          boolean giftWrap, boolean expressDelivery, boolean insurance) {
        ShoppingCart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) throw new IllegalArgumentException("The cart is empty.");

        for (CartItem item : cart.getItems()) {
            if (item.getQuantity() > item.getProduct().getStock()) {
                throw new IllegalArgumentException("Not enough stock for " + item.getProduct().getName());
            }
        }

        double subtotal = cart.getSubtotal();
        DiscountCalculator calculator = new DiscountCalculator(strategy);
        double discount = calculator.calculate(subtotal, cart.getTotalQuantity());
        double priceAfterDiscount = subtotal - discount;

        OrderPrice orderPrice = new BasicOrder(priceAfterDiscount);
        if (giftWrap) orderPrice = new GiftWrapDecorator(orderPrice);
        if (expressDelivery) orderPrice = new ExpressDeliveryDecorator(orderPrice);
        if (insurance) orderPrice = new InsuranceDecorator(orderPrice);

        double total = orderPrice.getCost();
        if (!payment.processPayment(total)) {
            throw new IllegalArgumentException("Payment was rejected. The cart was not changed.");
        }

        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            orderItems.add(new OrderItem(product.getId(), product.getName(), product.getPrice(),
                    item.getQuantity()));
            product.setStock(product.getStock() - item.getQuantity());
        }

        String status = payment.getPaymentMethod().equals("Cash on delivery") ? "CREATED" : "PAID";
        Order order = new Order(nextOrderId(), userId, orderItems, subtotal, discount,
                total - priceAfterDiscount, total, strategy.getName(), orderPrice.getDescription(),
                payment.getPaymentMethod(), status, currentDate());
        data.orders.add(order);
        cart.clear();

        String message = "Order #" + order.getId() + " confirmed. Total: $"
                + String.format("%.2f", total) + ". Status: " + status;
        data.notifications.add(new Notification(nextNotificationId(), userId, message, currentDate()));
        System.out.println("Notification: " + message);
        dataManager.saveData(data);
        return order;
    }

    public ArrayList<Order> getCustomerOrders(int userId) {
        ArrayList<Order> result = new ArrayList<>();
        for (Order order : data.orders) if (order.getUserId() == userId) result.add(order);
        return result;
    }

    public ArrayList<Order> getAllOrders() {
        return data.orders;
    }

    public Order findOrder(int orderId) {
        for (Order order : data.orders) if (order.getId() == orderId) return order;
        throw new IllegalArgumentException("Order not found.");
    }

    public void cancelOrder(int userId, int orderId) {
        Order order = findOrder(orderId);
        if (order.getUserId() != userId) throw new IllegalArgumentException("Order not found.");
        if (order.getStatus().equals("SHIPPED") || order.getStatus().equals("DELIVERED")
                || order.getStatus().equals("CANCELLED")) {
            throw new IllegalArgumentException("This order cannot be cancelled.");
        }
        restoreStock(order);
        order.setStatus("CANCELLED");
        addStatusNotification(order);
        dataManager.saveData(data);
    }

    public void updateStatus(int orderId, String newStatus) {
        Order order = findOrder(orderId);
        String status = newStatus.trim().toUpperCase();
        if (!isAllowed(order.getStatus(), status)) {
            throw new IllegalArgumentException("Cannot change " + order.getStatus() + " to " + status + ".");
        }
        if (status.equals("CANCELLED")) restoreStock(order);
        order.setStatus(status);
        addStatusNotification(order);
        dataManager.saveData(data);
    }

    public ArrayList<Notification> getNotifications(int userId) {
        ArrayList<Notification> result = new ArrayList<>();
        for (Notification n : data.notifications) if (n.getUserId() == userId) result.add(n);
        return result;
    }

    private boolean isAllowed(String oldStatus, String newStatus) {
        if (newStatus.equals("CANCELLED")) {
            return oldStatus.equals("CREATED") || oldStatus.equals("PAID")
                    || oldStatus.equals("PROCESSING");
        }
        if (oldStatus.equals("CREATED")) return newStatus.equals("PAID") || newStatus.equals("PROCESSING");
        if (oldStatus.equals("PAID")) return newStatus.equals("PROCESSING");
        if (oldStatus.equals("PROCESSING")) return newStatus.equals("SHIPPED");
        if (oldStatus.equals("SHIPPED")) return newStatus.equals("DELIVERED");
        return false;
    }

    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            for (Product product : data.products) {
                if (product.getId() == item.getProductId()) {
                    product.setStock(product.getStock() + item.getQuantity());
                }
            }
        }
    }

    private void addStatusNotification(Order order) {
        String message = "Order #" + order.getId() + " status changed to " + order.getStatus() + ".";
        data.notifications.add(new Notification(nextNotificationId(), order.getUserId(), message, currentDate()));
    }

    private int nextOrderId() {
        int max = 0;
        for (Order order : data.orders) if (order.getId() > max) max = order.getId();
        return max + 1;
    }

    private int nextNotificationId() {
        int max = 0;
        for (Notification n : data.notifications) if (n.getId() > max) max = n.getId();
        return max + 1;
    }

    private String currentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
