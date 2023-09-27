package models.user;

import java.io.Serializable;

public abstract class User implements Serializable {
    // Attributes
    private String username;
    private String password; // Consider hashing this password in a real-world application.

    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {}

    // Methods
    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Consider removing this getter for better security.
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}