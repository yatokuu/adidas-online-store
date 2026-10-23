package model;

public class Product {
    private int id;
    private String name;
    private String category;
    private String description;
    private double price;
    private int stock;

    public Product(int id, String name, String category, String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return id + ". " + name + " | " + category + " | $" + String.format("%.2f", price)
                + " | stock: " + stock + "\n   " + description;
    }
}
