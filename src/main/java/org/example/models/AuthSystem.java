package org.example.models;

import java.util.HashMap;
import java.util.Map;
import org.example.enums.Role;
import org.example.interfaces.IAuthSystem;

public class AuthSystem implements IAuthSystem {
    private final Map<String, User> users = new HashMap<>();
    private User currentUser;

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
        JsonUtil.appendToJson(new User(username, password, role), "users.json", User.class);
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void removeUser(String username) {
        users.remove(username);
        JsonUtil.removeFromJson("users.json", User.class, item -> item.getUsername().equals(username));
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isAdmin(String username) {
        User user = users.get(username);
        return user != null && user.getRole() == Role.ADMIN;
    }

    public void updateUsername(String oldUsername, String newUsername) {
        User user = users.get(oldUsername);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }
        users.remove(oldUsername);
        user.setUsername(newUsername);
        users.put(newUsername, user);
    }

    public void updatePassword(String username, String newPassword) {
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }
        user.setPassword(newPassword);
    }

    public void updateRole(String username, Role newRole) {
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }
        user.setRole(newRole);
    }
}

