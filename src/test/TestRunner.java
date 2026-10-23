package test;

import adapter.*;
import data.DataManager;
import data.StoreData;
import decorator.*;
import model.Product;
import model.User;
import service.*;
import strategy.*;

import java.io.File;

public class TestRunner {
    private static int passed = 0;
    private static int failed = 0;
    private static int folderNumber = 1;

    public static void main(String[] args) {
        run("Register customer and reject duplicate email", TestRunner::testRegistration);
        run("Search and filter products", TestRunner::testSearch);
        run("Add and update shopping cart", TestRunner::testCart);
        run("Reject quantity larger than stock", TestRunner::testStockValidation);
        run("Checkout reduces stock and clears cart", TestRunner::testCheckout);
        run("Failed payment keeps the cart", TestRunner::testFailedPayment);
        run("Cancellation restores product stock", TestRunner::testCancellation);
        run("Order data remains after reload", TestRunner::testPersistence);
        run("Strategy pattern switches algorithms", TestRunner::testStrategyPattern);
        run("Adapter converts dollars to cents", TestRunner::testAdapterPattern);
        run("Decorator pattern combines three extras", TestRunner::testDecoratorPattern);

        System.out.println("\nTEST RESULT: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    private static void testRegistration() {
        Fixture f = new Fixture();
        User customer = f.users.register("Aruzhan", "aruzhan@test.com", "123456");
        check(customer.getRole().equals("CUSTOMER"), "Role should be CUSTOMER");
        expectError(() -> f.users.register("Other", "aruzhan@test.com", "123456"));
    }

    private static void testSearch() {
        Fixture f = new Fixture();
        check(f.products.search("samba", "Shoes", 150).size() == 1,
                "Samba should match all filters");
        check(f.products.search("", "Clothing", 0).size() == 1,
                "One clothing product expected");
    }

    private static void testCart() {
        Fixture f = new Fixture();
        f.carts.addProduct(1, 1, 2);
        f.carts.updateQuantity(1, 1, 3);
        check(f.carts.getCart(1).getTotalQuantity() == 3, "Quantity should be 3");
        checkClose(f.carts.getCart(1).getSubtotal(), 300, "Subtotal should be 300");
    }

    private static void testStockValidation() {
        Fixture f = new Fixture();
        expectError(() -> f.carts.addProduct(1, 1, 20));
    }

    private static void testCheckout() {
        Fixture f = new Fixture();
        f.carts.addProduct(1, 1, 2);
        f.orders.checkout(1, new SeasonalDiscountStrategy(), new CashOnDeliveryPayment(),
                true, false, false);
        check(f.products.findById(1).getStock() == 8, "Stock should decrease from 10 to 8");
        check(f.carts.getCart(1).getItems().isEmpty(), "Cart should be empty");
        check(f.data.orders.size() == 1, "Order should be stored");
        check(f.data.notifications.size() == 1, "Confirmation notification should be stored");
    }

    private static void testFailedPayment() {
        Fixture f = new Fixture();
        f.carts.addProduct(1, 1, 1);
        expectError(() -> f.orders.checkout(1, new NoDiscountStrategy(),
                new CardPaymentAdapter(new ExternalPaymentSystem(), "11110000"),
                false, false, false));
        check(f.carts.getCart(1).getTotalQuantity() == 1, "Cart must stay unchanged");
        check(f.products.findById(1).getStock() == 10, "Stock must stay unchanged");
    }

    private static void testCancellation() {
        Fixture f = new Fixture();
        f.carts.addProduct(1, 1, 2);
        int orderId = f.orders.checkout(1, new NoDiscountStrategy(),
                new CashOnDeliveryPayment(), false, false, false).getId();
        f.orders.cancelOrder(1, orderId);
        check(f.products.findById(1).getStock() == 10, "Cancellation should restore stock");
        check(f.orders.findOrder(orderId).getStatus().equals("CANCELLED"),
                "Status should be CANCELLED");
    }

    private static void testPersistence() {
        Fixture f = new Fixture();
        f.carts.addProduct(1, 1, 1);
        f.orders.checkout(1, new NoDiscountStrategy(), new CashOnDeliveryPayment(),
                false, false, false);
        StoreData loadedAgain = f.manager.loadData();
        check(loadedAgain.orders.size() == 1, "Saved order should be loaded from file");
        check(loadedAgain.products.get(0).getStock() == 9, "Saved stock should be loaded");
    }

    private static void testStrategyPattern() {
        DiscountCalculator calculator = new DiscountCalculator(new NoDiscountStrategy());
        checkClose(calculator.calculate(100, 3), 0, "No discount");
        calculator.setStrategy(new SeasonalDiscountStrategy());
        checkClose(calculator.calculate(100, 3), 10, "Seasonal discount");
        calculator.setStrategy(new BulkDiscountStrategy());
        checkClose(calculator.calculate(100, 3), 8, "Bulk discount");
    }

    private static void testAdapterPattern() {
        RecordingExternalSystem external = new RecordingExternalSystem();
        PaymentProcessor adapter = new CardPaymentAdapter(external, "12345678");
        check(adapter.processPayment(12.34), "Payment should pass");
        check(external.receivedCents == 1234, "Adapter should convert 12.34 to 1234 cents");
    }

    private static void testDecoratorPattern() {
        OrderPrice price = new BasicOrder(100);
        price = new GiftWrapDecorator(price);
        price = new ExpressDeliveryDecorator(price);
        price = new InsuranceDecorator(price);
        checkClose(price.getCost(), 128, "Three decorators should add 28");
        check(price.getDescription().contains("gift wrap")
                        && price.getDescription().contains("express delivery")
                        && price.getDescription().contains("insurance"),
                "Description should contain every extra");
    }

    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("PASS: " + name);
        } catch (Throwable error) {
            failed++;
            System.out.println("FAIL: " + name + " - " + error.getMessage());
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void checkClose(double actual, double expected, String message) {
        if (Math.abs(actual - expected) > 0.001) {
            throw new AssertionError(message + ": expected " + expected + ", got " + actual);
        }
    }

    private static void expectError(Runnable action) {
        boolean errorThrown = false;
        try {
            action.run();
        } catch (IllegalArgumentException e) {
            errorThrown = true;
        }
        check(errorThrown, "Expected an IllegalArgumentException");
    }

    private static class Fixture {
        DataManager manager;
        StoreData data;
        UserService users;
        ProductService products;
        CartService carts;
        OrderService orders;

        Fixture() {
            File folder = new File("build/test-data-" + folderNumber++);
            folder.mkdirs();
            manager = new DataManager(folder.getPath());
            data = new StoreData();
            data.users.add(new User(1, "Test Customer", "customer@test.com", "123456", "CUSTOMER"));
            data.products.add(new Product(1, "Samba", "Shoes", "Classic shoe", 100, 10));
            data.products.add(new Product(2, "Hoodie", "Clothing", "Warm hoodie", 50, 5));
            manager.saveData(data);
            users = new UserService(data, manager);
            products = new ProductService(data, manager);
            carts = new CartService(data, manager, products);
            orders = new OrderService(data, manager, carts);
        }
    }

    private static class RecordingExternalSystem extends ExternalPaymentSystem {
        int receivedCents;

        @Override
        public boolean makePayment(String cardNumber, int amountInCents) {
            receivedCents = amountInCents;
            return true;
        }
    }
}
