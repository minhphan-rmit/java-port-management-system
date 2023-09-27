package models.user;

// Use singleton pattern if there is only one admin
public class SystemAdmin extends User {
    private static SystemAdmin instance;

    private SystemAdmin(String username, String password) {
        super(username, password);
    }

    public static SystemAdmin getInstance() {
        if (instance == null) {
            instance = new SystemAdmin("admin", "admin"); // This is still a security concern
        }
        return instance;
    }
}