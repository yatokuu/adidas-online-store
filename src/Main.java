import data.DataManager;
import data.StoreData;
import service.CartService;
import service.OrderService;
import service.ProductService;
import service.UserService;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        try {
            DataManager dataManager = new DataManager("data");
            StoreData data = dataManager.loadData();

            UserService userService = new UserService(data, dataManager);
            ProductService productService = new ProductService(data, dataManager);
            CartService cartService = new CartService(data, dataManager, productService);
            OrderService orderService = new OrderService(data, dataManager, cartService);

            ConsoleUI ui = new ConsoleUI(userService, productService, cartService, orderService);
            ui.start();
        } catch (Exception e) {
            System.out.println("The application could not start: " + e.getMessage());
        }
    }
}
