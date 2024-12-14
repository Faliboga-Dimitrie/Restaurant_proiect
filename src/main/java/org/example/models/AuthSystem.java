package org.example.models;

import java.util.HashMap;
import java.util.Map;
import org.example.enums.Role;

public class AuthSystem {
    private final Map<String, User> users = new HashMap<>();

    public AuthSystem() {}

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void register(String username, String password, Role role) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists!");
        }
        users.put(username, new User(username, password, role));
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public boolean isAdmin(String username) {
        User user = users.get(username);
        return user != null && user.getRole() == Role.ADMIN;
    }
}

