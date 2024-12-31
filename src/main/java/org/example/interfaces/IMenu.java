package org.example.interfaces;

import org.example.models.Drink;
import org.example.models.Food;
import org.example.models.MenuItem;

public interface IMenu {
    void addDrink(Drink drink, boolean fromJson);
    void addFood(Food food, boolean fromJson);
    void removeDrink(Drink drink);
    void removeFood(Food food);
    MenuItem findMenuItemByName(String name);
    void displayMenu();
}
