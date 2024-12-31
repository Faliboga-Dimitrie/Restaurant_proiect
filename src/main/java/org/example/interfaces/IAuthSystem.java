package org.example.interfaces;

import org.example.enums.Role;
import org.example.models.User;

public interface IAuthSystem {
    void register(String username, String password, Role role);
    User authenticate(String username, String password);
    void addUser(User user);
    void removeUser(String username);
    boolean isAdmin(String username);
    User getCurrentUser();
    void setCurrentUser(User currentUser);
}
