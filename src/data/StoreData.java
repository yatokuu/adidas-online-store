package data;

import model.Notification;
import model.Order;
import model.Product;
import model.ShoppingCart;
import model.User;

import java.util.ArrayList;

public class StoreData {
    public ArrayList<Product> products = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<ShoppingCart> carts = new ArrayList<>();
    public ArrayList<Order> orders = new ArrayList<>();
    public ArrayList<Notification> notifications = new ArrayList<>();
}
