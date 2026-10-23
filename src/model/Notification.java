package model;

public class Notification {
    private int id;
    private int userId;
    private String message;
    private String date;

    public Notification(int id, int userId, String message, String date) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.date = date;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getDate() { return date; }
}
