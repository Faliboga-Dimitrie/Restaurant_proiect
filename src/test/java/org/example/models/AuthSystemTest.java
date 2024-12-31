package org.example.models;

import org.example.enums.Role;

import static org.junit.jupiter.api.Assertions.*;

class AuthSystemTest {

    @org.junit.jupiter.api.Test
    void authenticate() {
        AuthSystem authSystem = new AuthSystem();
        User user = new User("test", "test", Role.ADMIN);
        authSystem.addUser(user);

        User authenticatedUser = authSystem.authenticate("test", "test");
        assertNotNull(authenticatedUser);
        assertEquals((user.getUsername()), authenticatedUser.getUsername());

        authenticatedUser = authSystem.authenticate("testUser", "wrongPass");
        assertNull(authenticatedUser);

        authenticatedUser = authSystem.authenticate("nonexistentUser", "anyPass");
        assertNull(authenticatedUser);
    }

    @org.junit.jupiter.api.Test
    void register() {

        AuthSystem authSystem = new AuthSystem();
        authSystem.register("test", "test", Role.ADMIN);
        User user = authSystem.getUsers().get("test");
        assertNotNull(user);
        assertEquals("test", user.getUsername());
        assertEquals("test", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());

        assertThrows(IllegalArgumentException.class, () -> authSystem.register("test", "test", Role.ADMIN));
    }
}