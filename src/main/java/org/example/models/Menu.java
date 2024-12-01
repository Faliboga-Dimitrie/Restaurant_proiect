package org.example.models;

import java.util.HashMap;
import org.example.enums.MenuUpdateType;
import java.util.Scanner;

public class Menu {
    private HashMap<String,MenuItem> menuItems;
    private boolean toAdd = false;
    private boolean toIngredient = false;

    public Menu() {
        menuItems = new HashMap<>();
    }

    public Menu(HashMap<String,MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public HashMap<String,MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(HashMap<String,MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.put(menuItem.getName(), menuItem);
    }

    public void removeMenuItem(MenuItem menuItem) {
        menuItems.remove(menuItem);
    }

    public MenuItem findItemByName(String menuItemName) {
        return menuItems.get(menuItemName);
    }

    private void interogateUser() {
        try (Scanner scanner = new Scanner(System.in)) {
            toAdd = askUser(scanner, "Do you want to update " +
                    "the menu items? Enter 'true' or 'false':");
            if (!toAdd) {
                toIngredient = askUser(scanner, "Do you want to update" +
                        " the item? Enter 'true' or 'false':");
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean askUser(Scanner scanner, String question) {
        System.out.println(question);
        while (true) {
            try {
                return scanner.nextBoolean();
            } catch (Exception e) {
                System.out.println("Please enter 'true' or 'false'.");
                scanner.nextLine();
            }
        }
    }
    public <T> void updateMenuItem(String itemName, MenuUpdateType updateType, T newValue) {
        MenuItem item = findItemByName(itemName);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        switch (updateType) {
            case NAME:
                if (newValue instanceof String) {
                    item.setName((String) newValue);
                } else {
                    throw new IllegalArgumentException("Invalid value type for NAME. Expected String.");
                }
                break;
            case PRICE:
                if (newValue instanceof Double) {
                    item.setPrice((Double) newValue);
                } else {
                    throw new IllegalArgumentException("Invalid value type for PRICE. Expected Double.");
                }
                break;
            case INGREDIENTS:
                if (newValue instanceof Ingredient) {
                    interogateUser();
                    item.modifyIngredients((Ingredient) newValue,toAdd,toIngredient,item.findIngredientByName(((Ingredient) newValue).getName()));
                } else {
                    throw new IllegalArgumentException("Invalid value type for CATEGORY. Expected String.");
                }
                break;
            case DESCRIPTION:
                if (newValue instanceof String) {
                    item.setDescription((String) newValue);
                } else {
                    throw new IllegalArgumentException("Invalid value type for DESCRIPTION. Expected String.");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported update type: " + updateType);
        }
    }

}
