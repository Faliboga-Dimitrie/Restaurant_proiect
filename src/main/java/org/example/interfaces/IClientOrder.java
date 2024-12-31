package org.example.interfaces;

import org.example.models.MenuItem;

import java.time.LocalDateTime;

public interface IClientOrder {
    void addItem(MenuItem item, int quantity);
    void removeItem(MenuItem item, int quantity);
    String getClientName();
    LocalDateTime getOrderTime();
}
