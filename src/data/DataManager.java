package data;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class DataManager {
    private File dataFolder;

    public DataManager(String folderName) {
        dataFolder = new File(folderName);
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public StoreData loadData() {
        StoreData data = new StoreData();
        loadProducts(data);
        loadUsers(data);
        loadCarts(data);
        loadOrders(data);
        loadNotifications(data);
        return data;
    }

    public void saveData(StoreData data) {
        saveProducts(data.products);
        saveUsers(data.users);
        saveCarts(data.carts);
        saveOrders(data.orders);
        saveNotifications(data.notifications);
    }

    private void loadProducts(StoreData data) {
        for (String line : readLines("products.txt")) {
            String[] p = line.split("\\|", -1);
            if (p.length == 6) {
                data.products.add(new Product(Integer.parseInt(p[0]), p[1], p[2], p[3],
                        Double.parseDouble(p[4]), Integer.parseInt(p[5])));
            }
        }
    }

    private void loadUsers(StoreData data) {
        for (String line : readLines("users.txt")) {
            String[] p = line.split("\\|", -1);
            if (p.length == 5) {
                data.users.add(new User(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4]));
            }
        }
    }

    private void loadCarts(StoreData data) {
        for (String line : readLines("carts.txt")) {
            String[] p = line.split("\\|", -1);
            if (p.length != 2) continue;
            ShoppingCart cart = new ShoppingCart(Integer.parseInt(p[0]));
            if (!p[1].isBlank()) {
                String[] savedItems = p[1].split(";");
                for (String savedItem : savedItems) {
                    String[] values = savedItem.split(":");
                    Product product = findProduct(data.products, Integer.parseInt(values[0]));
                    if (product != null) cart.addProduct(product, Integer.parseInt(values[1]));
                }
            }
            data.carts.add(cart);
        }
    }

    private void loadOrders(StoreData data) {
        for (String line : readLines("orders.txt")) {
            String[] p = line.split("\\|", -1);
            if (p.length != 12) continue;
            ArrayList<OrderItem> items = new ArrayList<>();
            if (!p[2].isBlank()) {
                for (String savedItem : p[2].split(";")) {
                    String[] values = savedItem.split(":", -1);
                    items.add(new OrderItem(Integer.parseInt(values[0]), values[1],
                            Double.parseDouble(values[2]), Integer.parseInt(values[3])));
                }
            }
            data.orders.add(new Order(Integer.parseInt(p[0]), Integer.parseInt(p[1]), items,
                    Double.parseDouble(p[3]), Double.parseDouble(p[4]), Double.parseDouble(p[5]),
                    Double.parseDouble(p[6]), p[7], p[8], p[9], p[10], p[11]));
        }
    }

    private void loadNotifications(StoreData data) {
        for (String line : readLines("notifications.txt")) {
            String[] p = line.split("\\|", -1);
            if (p.length == 4) {
                data.notifications.add(new Notification(Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                        p[2], p[3]));
            }
        }
    }

    private void saveProducts(ArrayList<Product> products) {
        ArrayList<String> lines = new ArrayList<>();
        for (Product p : products) {
            lines.add(p.getId() + "|" + clean(p.getName()) + "|" + clean(p.getCategory()) + "|"
                    + clean(p.getDescription()) + "|" + number(p.getPrice()) + "|" + p.getStock());
        }
        writeLines("products.txt", lines);
    }

    private void saveUsers(ArrayList<User> users) {
        ArrayList<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(u.getId() + "|" + clean(u.getName()) + "|" + clean(u.getEmail()) + "|"
                    + clean(u.getPassword()) + "|" + u.getRole());
        }
        writeLines("users.txt", lines);
    }

    private void saveCarts(ArrayList<ShoppingCart> carts) {
        ArrayList<String> lines = new ArrayList<>();
        for (ShoppingCart cart : carts) {
            String items = "";
            for (CartItem item : cart.getItems()) {
                if (!items.isEmpty()) items += ";";
                items += item.getProduct().getId() + ":" + item.getQuantity();
            }
            lines.add(cart.getUserId() + "|" + items);
        }
        writeLines("carts.txt", lines);
    }

    private void saveOrders(ArrayList<Order> orders) {
        ArrayList<String> lines = new ArrayList<>();
        for (Order order : orders) {
            String items = "";
            for (OrderItem item : order.getItems()) {
                if (!items.isEmpty()) items += ";";
                items += item.getProductId() + ":" + cleanItem(item.getProductName()) + ":"
                        + number(item.getPrice()) + ":" + item.getQuantity();
            }
            lines.add(order.getId() + "|" + order.getUserId() + "|" + items + "|"
                    + number(order.getSubtotal()) + "|" + number(order.getDiscount()) + "|"
                    + number(order.getExtrasCost()) + "|" + number(order.getTotal()) + "|"
                    + clean(order.getDiscountName()) + "|"
                    + clean(order.getExtras()) + "|" + clean(order.getPaymentMethod()) + "|"
                    + order.getStatus() + "|" + clean(order.getDate()));
        }
        writeLines("orders.txt", lines);
    }

    private void saveNotifications(ArrayList<Notification> notifications) {
        ArrayList<String> lines = new ArrayList<>();
        for (Notification n : notifications) {
            lines.add(n.getId() + "|" + n.getUserId() + "|" + clean(n.getMessage()) + "|"
                    + clean(n.getDate()));
        }
        writeLines("notifications.txt", lines);
    }

    private ArrayList<String> readLines(String fileName) {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(dataFolder, fileName);
        if (!file.exists()) return lines;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank() && !line.startsWith("#")) lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read " + fileName, e);
        }
        return lines;
    }

    private void writeLines(String fileName, ArrayList<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(dataFolder, fileName)))) {
            for (String line : lines) writer.println(line);
        } catch (IOException e) {
            throw new RuntimeException("Could not save " + fileName, e);
        }
    }

    private Product findProduct(ArrayList<Product> products, int id) {
        for (Product product : products) if (product.getId() == id) return product;
        return null;
    }

    private String clean(String value) {
        return value.replace("|", "/").replace("\n", " ");
    }

    private String cleanItem(String value) {
        return clean(value).replace(":", "-").replace(";", "-");
    }

    private String number(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}
