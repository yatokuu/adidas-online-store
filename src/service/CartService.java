package service;

import data.DataManager;
import data.StoreData;
import model.CartItem;
import model.Product;
import model.ShoppingCart;

public class CartService {
    private StoreData data;
    private DataManager dataManager;
    private ProductService productService;

    public CartService(StoreData data, DataManager dataManager, ProductService productService) {
        this.data = data;
        this.dataManager = dataManager;
        this.productService = productService;
    }

    public ShoppingCart getCart(int userId) {
        for (ShoppingCart cart : data.carts) {
            if (cart.getUserId() == userId) return cart;
        }
        ShoppingCart cart = new ShoppingCart(userId);
        data.carts.add(cart);
        return cart;
    }

    public void addProduct(int userId, int productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        Product product = productService.findById(productId);
        ShoppingCart cart = getCart(userId);
        CartItem oldItem = cart.findItem(productId);
        int newQuantity = quantity;
        if (oldItem != null) newQuantity += oldItem.getQuantity();
        checkStock(product, newQuantity);
        cart.addProduct(product, quantity);
        dataManager.saveData(data);
    }

    public void updateQuantity(int userId, int productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        ShoppingCart cart = getCart(userId);
        CartItem item = cart.findItem(productId);
        if (item == null) throw new IllegalArgumentException("Product is not in the cart.");
        checkStock(item.getProduct(), quantity);
        item.setQuantity(quantity);
        dataManager.saveData(data);
    }

    public void removeProduct(int userId, int productId) {
        ShoppingCart cart = getCart(userId);
        if (cart.findItem(productId) == null) {
            throw new IllegalArgumentException("Product is not in the cart.");
        }
        cart.removeProduct(productId);
        dataManager.saveData(data);
    }

    private void checkStock(Product product, int quantity) {
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Only " + product.getStock() + " items are available.");
        }
    }
}
