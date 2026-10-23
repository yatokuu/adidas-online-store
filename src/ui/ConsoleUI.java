package ui;

import adapter.*;
import model.*;
import service.*;
import strategy.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner = new Scanner(System.in);
    private UserService userService;
    private ProductService productService;
    private CartService cartService;
    private OrderService orderService;

    public ConsoleUI(UserService userService, ProductService productService,
                     CartService cartService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    public void start() {
        System.out.println("=================================");
        System.out.println("     ADIDAS ONLINE STORE DEMO");
        System.out.println("=================================");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Browse products as guest");
            System.out.println("0. Exit");
            int choice = readInt("Choose: ");
            try {
                if (choice == 1) register();
                else if (choice == 2) login();
                else if (choice == 3) printProducts(productService.getAllProducts());
                else if (choice == 0) running = false;
                else System.out.println("Unknown menu option.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Thank you for visiting the store.");
    }

    private void register() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        User user = userService.register(name, email, password);
        System.out.println("Account created for " + user.getName() + ".");
    }

    private void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        User user = userService.login(email, password);
        System.out.println("Welcome, " + user.getName() + "!");
        if (user.isAdmin()) adminMenu();
        else customerMenu(user);
    }

    private void customerMenu(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. Browse products");
            System.out.println("2. Search and filter products");
            System.out.println("3. View cart");
            System.out.println("4. Add product to cart");
            System.out.println("5. Change cart quantity");
            System.out.println("6. Remove product from cart");
            System.out.println("7. Checkout");
            System.out.println("8. Order history");
            System.out.println("9. Track order");
            System.out.println("10. Cancel order");
            System.out.println("11. View notifications");
            System.out.println("0. Logout");
            int choice = readInt("Choose: ");
            try {
                if (choice == 1) printProducts(productService.getAllProducts());
                else if (choice == 2) searchProducts();
                else if (choice == 3) printCart(user.getId());
                else if (choice == 4) addToCart(user.getId());
                else if (choice == 5) updateCart(user.getId());
                else if (choice == 6) removeFromCart(user.getId());
                else if (choice == 7) checkout(user.getId());
                else if (choice == 8) printOrders(orderService.getCustomerOrders(user.getId()));
                else if (choice == 9) trackOrder(user.getId());
                else if (choice == 10) cancelOrder(user.getId());
                else if (choice == 11) printNotifications(user.getId());
                else if (choice == 0) loggedIn = false;
                else System.out.println("Unknown menu option.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void adminMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View product catalog");
            System.out.println("2. Add product");
            System.out.println("3. Update stock");
            System.out.println("4. Update price");
            System.out.println("5. View all orders");
            System.out.println("6. Change order status");
            System.out.println("0. Logout");
            int choice = readInt("Choose: ");
            try {
                if (choice == 1) printProducts(productService.getAllProducts());
                else if (choice == 2) addProduct();
                else if (choice == 3) updateStock();
                else if (choice == 4) updatePrice();
                else if (choice == 5) printOrders(orderService.getAllOrders());
                else if (choice == 6) updateOrderStatus();
                else if (choice == 0) loggedIn = false;
                else System.out.println("Unknown menu option.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void searchProducts() {
        System.out.print("Search word (Enter for all): ");
        String word = scanner.nextLine();
        System.out.print("Category Shoes/Clothing/Accessories (Enter for all): ");
        String category = scanner.nextLine();
        double maxPrice = readDouble("Maximum price (0 for any): ");
        printProducts(productService.search(word, category, maxPrice));
    }

    private void addToCart(int userId) {
        int productId = readInt("Product ID: ");
        int quantity = readInt("Quantity: ");
        cartService.addProduct(userId, productId, quantity);
        System.out.println("Product added to cart.");
    }

    private void updateCart(int userId) {
        int productId = readInt("Product ID: ");
        int quantity = readInt("New quantity: ");
        cartService.updateQuantity(userId, productId, quantity);
        System.out.println("Cart updated.");
    }

    private void removeFromCart(int userId) {
        int productId = readInt("Product ID: ");
        cartService.removeProduct(userId, productId);
        System.out.println("Product removed.");
    }

    private void printCart(int userId) {
        ShoppingCart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) {
            System.out.println("The cart is empty.");
            return;
        }
        System.out.println("\n--- SHOPPING CART ---");
        for (CartItem item : cart.getItems()) {
            System.out.println(item.getProduct().getName() + " x " + item.getQuantity()
                    + " = $" + String.format("%.2f", item.getTotal()));
        }
        System.out.println("Subtotal: $" + String.format("%.2f", cart.getSubtotal()));
    }

    private void checkout(int userId) {
        printCart(userId);
        System.out.println("\nDiscount strategy:");
        System.out.println("1. No discount");
        System.out.println("2. Seasonal sale - 10%");
        System.out.println("3. Buy 3 or more items - 8%");
        int discountChoice = readInt("Choose: ");
        DiscountStrategy strategy;
        if (discountChoice == 1) strategy = new NoDiscountStrategy();
        else if (discountChoice == 2) strategy = new SeasonalDiscountStrategy();
        else if (discountChoice == 3) strategy = new BulkDiscountStrategy();
        else throw new IllegalArgumentException("Unknown discount option.");

        System.out.println("\nPayment method:");
        System.out.println("1. Bank card");
        System.out.println("2. Electronic wallet");
        System.out.println("3. Cash on delivery");
        int paymentChoice = readInt("Choose: ");
        PaymentProcessor payment;
        if (paymentChoice == 1) {
            System.out.print("Card number (use 0000 at the end to test rejection): ");
            payment = new CardPaymentAdapter(new ExternalPaymentSystem(), scanner.nextLine());
        } else if (paymentChoice == 2) {
            System.out.print("Wallet name (use 'blocked' to test rejection): ");
            payment = new WalletPayment(scanner.nextLine());
        } else if (paymentChoice == 3) {
            payment = new CashOnDeliveryPayment();
        } else {
            throw new IllegalArgumentException("Unknown payment option.");
        }

        boolean gift = askYesNo("Add gift wrap for $5? (y/n): ");
        boolean express = askYesNo("Add express delivery for $15? (y/n): ");
        boolean insurance = askYesNo("Add product insurance for $8? (y/n): ");

        Order order = orderService.checkout(userId, strategy, payment, gift, express, insurance);
        System.out.println("Order created successfully: " + order);
        System.out.println("Discount: $" + String.format("%.2f", order.getDiscount()));
        System.out.println("Optional services: " + order.getExtras());
    }

    private void trackOrder(int userId) {
        int id = readInt("Order ID: ");
        Order order = orderService.findOrder(id);
        if (order.getUserId() != userId) throw new IllegalArgumentException("Order not found.");
        printOrderDetails(order);
    }

    private void cancelOrder(int userId) {
        int id = readInt("Order ID to cancel: ");
        orderService.cancelOrder(userId, id);
        System.out.println("Order cancelled and stock restored.");
    }

    private void printNotifications(int userId) {
        ArrayList<Notification> notifications = orderService.getNotifications(userId);
        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }
        for (Notification n : notifications) {
            System.out.println(n.getDate() + " - " + n.getMessage());
        }
    }

    private void addProduct() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        double price = readDouble("Price: ");
        int stock = readInt("Stock: ");
        Product product = productService.addProduct(name, category, description, price, stock);
        System.out.println("Product created with ID " + product.getId() + ".");
    }

    private void updateStock() {
        int id = readInt("Product ID: ");
        int stock = readInt("New stock: ");
        productService.updateStock(id, stock);
        System.out.println("Stock updated.");
    }

    private void updatePrice() {
        int id = readInt("Product ID: ");
        double price = readDouble("New price: ");
        productService.updatePrice(id, price);
        System.out.println("Price updated.");
    }

    private void updateOrderStatus() {
        int id = readInt("Order ID: ");
        System.out.print("New status (PAID/PROCESSING/SHIPPED/DELIVERED/CANCELLED): ");
        String status = scanner.nextLine();
        orderService.updateStatus(id, status);
        System.out.println("Order status updated.");
    }

    private void printProducts(ArrayList<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.println("\n--- PRODUCT CATALOG ---");
        for (Product product : products) System.out.println(product);
    }

    private void printOrders(ArrayList<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        for (Order order : orders) printOrderDetails(order);
    }

    private void printOrderDetails(Order order) {
        System.out.println("\n" + order);
        for (OrderItem item : order.getItems()) {
            System.out.println("  " + item.getProductName() + " x " + item.getQuantity()
                    + " = $" + String.format("%.2f", item.getTotal()));
        }
        System.out.println("  Discount: " + order.getDiscountName() + " (-$"
                + String.format("%.2f", order.getDiscount()) + ")");
        System.out.println("  Extras: " + order.getExtras() + " (+$"
                + String.format("%.2f", order.getExtrasCost()) + ")");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private boolean askYesNo(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }
}
