package model;

import java.util.ArrayList;

public class ShoppingCart {
    private int userId;
    private ArrayList<CartItem> items = new ArrayList<>();

    public ShoppingCart(int userId) {
        this.userId = userId;
    }

    public int getUserId() { return userId; }
    public ArrayList<CartItem> getItems() { return items; }

    public void addProduct(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public CartItem findItem(int productId) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == productId) return item;
        }
        return null;
    }

    public void removeProduct(int productId) {
        CartItem item = findItem(productId);
        if (item != null) items.remove(item);
    }

    public double getSubtotal() {
        double total = 0;
        for (CartItem item : items) total += item.getTotal();
        return total;
    }

    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : items) total += item.getQuantity();
        return total;
    }

    public void clear() { items.clear(); }
}
