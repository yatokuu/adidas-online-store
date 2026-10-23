package service;

import data.DataManager;
import data.StoreData;
import model.User;

public class UserService {
    private StoreData data;
    private DataManager dataManager;

    public UserService(StoreData data, DataManager dataManager) {
        this.data = data;
        this.dataManager = dataManager;
    }

    public User register(String name, String email, String password) {
        if (name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email is not valid.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must have at least 6 characters.");
        }
        for (User user : data.users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("This email is already registered.");
            }
        }
        User user = new User(nextId(), name, email.toLowerCase(), password, "CUSTOMER");
        data.users.add(user);
        dataManager.saveData(data);
        return user;
    }

    public User login(String email, String password) {
        for (User user : data.users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Wrong email or password.");
    }

    private int nextId() {
        int max = 0;
        for (User user : data.users) if (user.getId() > max) max = user.getId();
        return max + 1;
    }
}
