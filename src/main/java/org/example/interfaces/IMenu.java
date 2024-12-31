package org.example.interfaces;

import org.example.enums.MenuItemType;
import org.example.enums.MenuUpdateType;
import org.example.models.Drink;
import org.example.models.Food;
import org.example.models.MenuItem;

public interface IMenu {
    void addDrink(Drink drink, boolean fromJson);
    void addFood(Food food, boolean fromJson);
    void removeDrink(Drink drink);
    void removeFood(Food food);
    MenuItem findMenuItemByName(String name);
    <T> void updateMenuItem(String itemName, MenuUpdateType updateType, MenuItemType menuItemType, T newValue);
    void displayMenu();
}
