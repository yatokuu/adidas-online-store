package service;

import data.DataManager;
import data.StoreData;
import model.Product;

import java.util.ArrayList;

public class ProductService {
    private StoreData data;
    private DataManager dataManager;

    public ProductService(StoreData data, DataManager dataManager) {
        this.data = data;
        this.dataManager = dataManager;
    }

    public ArrayList<Product> getAllProducts() {
        return data.products;
    }

    public Product findById(int id) {
        for (Product product : data.products) {
            if (product.getId() == id) return product;
        }
        throw new IllegalArgumentException("Product not found.");
    }

    public ArrayList<Product> search(String word, String category, double maximumPrice) {
        ArrayList<Product> results = new ArrayList<>();
        String text = word.toLowerCase();
        for (Product product : data.products) {
            boolean matchesText = text.isBlank()
                    || product.getName().toLowerCase().contains(text)
                    || product.getDescription().toLowerCase().contains(text);
            boolean matchesCategory = category.isBlank()
                    || product.getCategory().equalsIgnoreCase(category);
            boolean matchesPrice = maximumPrice <= 0 || product.getPrice() <= maximumPrice;
            if (matchesText && matchesCategory && matchesPrice) results.add(product);
        }
        return results;
    }

    public Product addProduct(String name, String category, String description, double price, int stock) {
        if (name.isBlank() || category.isBlank() || description.isBlank()) {
            throw new IllegalArgumentException("Name, category, and description are required.");
        }
        validatePriceAndStock(price, stock);
        Product product = new Product(nextId(), name, category, description, price, stock);
        data.products.add(product);
        dataManager.saveData(data);
        return product;
    }

    public void updateStock(int productId, int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        findById(productId).setStock(stock);
        dataManager.saveData(data);
    }

    public void updatePrice(int productId, double price) {
        if (price <= 0) throw new IllegalArgumentException("Price must be positive.");
        findById(productId).setPrice(price);
        dataManager.saveData(data);
    }

    private void validatePriceAndStock(double price, int stock) {
        if (price <= 0) throw new IllegalArgumentException("Price must be positive.");
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative.");
    }

    private int nextId() {
        int max = 0;
        for (Product product : data.products) if (product.getId() > max) max = product.getId();
        return max + 1;
    }
}
