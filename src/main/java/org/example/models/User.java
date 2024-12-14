package org.example.models;

import org.example.enums.Role;

public class User {
    private final String username;
    private final String password;
    private final Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {
        this.username = "";
        this.password = "";
        this.role = Role.CLIENT;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role=" + role + "}";
    }
}

