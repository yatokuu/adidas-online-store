package model;

import java.util.ArrayList;

public class Order {
    private int id;
    private int userId;
    private ArrayList<OrderItem> items;
    private double subtotal;
    private double discount;
    private double extrasCost;
    private double total;
    private String discountName;
    private String extras;
    private String paymentMethod;
    private String status;
    private String date;

    public Order(int id, int userId, ArrayList<OrderItem> items, double subtotal, double discount,
                 double extrasCost, double total, String discountName, String extras,
                 String paymentMethod, String status, String date) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.subtotal = subtotal;
        this.discount = discount;
        this.extrasCost = extrasCost;
        this.total = total;
        this.discountName = discountName;
        this.extras = extras;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.date = date;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public ArrayList<OrderItem> getItems() { return items; }
    public double getSubtotal() { return subtotal; }
    public double getDiscount() { return discount; }
    public double getExtrasCost() { return extrasCost; }
    public double getTotal() { return total; }
    public String getDiscountName() { return discountName; }
    public String getExtras() { return extras; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getDate() { return date; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Order #" + id + " | " + date + " | $" + String.format("%.2f", total)
                + " | " + status + " | " + paymentMethod;
    }
}
