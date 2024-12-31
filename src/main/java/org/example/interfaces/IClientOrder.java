package org.example.interfaces;

import org.example.models.Food;
import org.example.models.Drink;
import org.example.models.MenuItem;
import org.example.models.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface IClientOrder {
    ArrayList<Pair<Food,Integer>> getFoodItems();
    double getTotalPrice();
    int getQuantity(MenuItem item);
    void addItem(MenuItem item, int quantity);
    void removeItem(MenuItem item, int quantity);
    String getClientName();
    LocalDateTime getOrderTime();
    ArrayList<Pair<Drink, Integer>> getDrinkItems();
    void changeQuantity(MenuItem item, int quantity);
}
